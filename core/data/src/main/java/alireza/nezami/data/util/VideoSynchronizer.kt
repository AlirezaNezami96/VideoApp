package alireza.nezami.data.util

import alireza.nezami.database.dao.VideoDao
import alireza.nezami.network.data_source.VideoDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class VideoSynchronizer @Inject constructor(
        private val localDataSource: VideoDao,
        private val remoteDataSource: VideoDataSource,
        private val coroutineScope: CoroutineScope,
        private val mapper: VideoMapper,
) {
    private val synchronizationState: MutableStateFlow<SynchronizationState> =
        MutableStateFlow(SynchronizationState.Idle)

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
            localDataSource.getAllVideos().map { localVideos ->
                if (localVideos.isEmpty()) {
                    remoteDataSource.getPopularVideos(page = 1, perPage = 20).map { remoteData ->
                        if (remoteData.hits?.isNotEmpty() == true) {
                            val mappedData =
                                mapper.mapToEntities(remoteData.hits.orEmpty()) // Insert videos and maintain bookmark status
                            val bookmarkedIds = localVideos.filter { it.isBookmarked }.map { it.id }
                            if (bookmarkedIds.isNotEmpty()) {
                                localDataSource.updateBookmarkedStatus(bookmarkedIds)
                            }
                            synchronizationState.value = SynchronizationState.Success(mappedData)
                        } else {
                            synchronizationState.value =
                                SynchronizationState.Error("Failed to fetch remote data")
                        }
                    }.catch {
                        synchronizationState.value =
                            SynchronizationState.Error("Failed to fetch remote data")
                    }.collect()
                } else {
                    synchronizationState.value = SynchronizationState.Success(localVideos)
                }
            }.catch {
                synchronizationState.value =
                    SynchronizationState.Error("Failed to fetch local data")
                Timber.e("Error syncing videos: ${it.message}")
            }.collect()

        } catch (e: Exception) {
            synchronizationState.value = SynchronizationState.Error(e.message ?: "Unknown error")
        }
    }
}