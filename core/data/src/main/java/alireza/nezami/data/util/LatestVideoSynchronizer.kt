package alireza.nezami.data.util

import alireza.nezami.database.dao.VideoDao
import alireza.nezami.model.data.VideoOrder
import alireza.nezami.network.data_source.VideoDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class LatestVideoSynchronizer @Inject constructor(
    private val localDataSource: VideoDao,
    private val remoteDataSource: VideoDataSource,
    private val coroutineScope: CoroutineScope,
    private val mapper: VideoMapper,
) {
    private val synchronizationState = 
        MutableStateFlow<SynchronizationState>(SynchronizationState.Idle)

    val synchronizationStateFlow: StateFlow<SynchronizationState>
        get() = synchronizationState

    fun startSynchronization() {
        coroutineScope.launch {
            synchronize()
        }
    }

    fun stopSynchronization() {
        coroutineScope.cancel()
    }

    private suspend fun synchronize() {
        synchronizationState.value = SynchronizationState.InProgress

        try {
            // Get current bookmarked IDs first
            val bookmarkedIds = 
                localDataSource.getAllLatestVideos().first()
                    .filter { it.isBookmarked }
                    .map { it.id }

            // Clear existing latest videos
            localDataSource.deleteVideosByType(VideoOrder.LATEST.value)

            // Fetch and save new data
            remoteDataSource.getLatestVideos(page = 1, perPage = 20).catch {
                synchronizationState.value = 
                    SynchronizationState.Error("Failed to fetch remote data")
            }.collect { remoteData ->
                if (remoteData.hits?.isNotEmpty() == true) {
                    val mappedData = mapper.mapToEntities(
                        videoResponses = remoteData.hits.orEmpty(),
                        order = VideoOrder.LATEST
                    )

                    // Save to local database
                    localDataSource.insertVideos(mappedData)

                    // Restore bookmark status
                    if (bookmarkedIds.isNotEmpty()) {
                        localDataSource.updateBookmarkedStatus(bookmarkedIds)
                    }

                    // Emit updated data
                    synchronizationState.value = SynchronizationState.Success(
                        localDataSource.getAllLatestVideos().first()
                    )
                } else {
                    synchronizationState.value = SynchronizationState.Error("No data available")
                }
            }
        } catch (e: Exception) {
            synchronizationState.value = SynchronizationState.Error(e.message ?: "Unknown error")
        }
    }
}