package alireza.nezami.data.util

import alireza.nezami.database.dao.BookmarkDao
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
        private val localVideoDataSource: VideoDao,
        private val localBookmarkDataSource: BookmarkDao,
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
            val localVideos = localVideoDataSource.getAllLatestVideos().first()
            val localBookmarks = localBookmarkDataSource.getAllBookmarks().first()

            if (localVideos.isNotEmpty()) {
                synchronizationState.value = SynchronizationState.Success(localVideos)
            }

            val bookmarkedIds = localBookmarks.map { it.id }

            remoteDataSource.getLatestVideos(page = 1, perPage = 20).catch {
                if (localVideos.isEmpty()) {
                    synchronizationState.value =
                        SynchronizationState.Error("Failed to fetch remote data")
                }
            }.collect { remoteData ->
                if (remoteData.hits?.isNotEmpty() == true) {
                    val mappedData = mapper.mapToEntities(
                        videoResponses = remoteData.hits.orEmpty(), order = VideoOrder.LATEST
                    )

                    localVideoDataSource.deleteVideosByType(VideoOrder.LATEST.value)
                    localVideoDataSource.insertVideos(mappedData)

                    if (bookmarkedIds.isNotEmpty()) {
                        mappedData.forEach { video ->
                            if (bookmarkedIds.contains(video.id)) {
                                video.isBookmarked = true
                            }
                        }
                    }

                    synchronizationState.value = SynchronizationState.Success(mappedData)
                } else if (localVideos.isEmpty()) {
                    synchronizationState.value = SynchronizationState.Error("No data available")
                }
            }
        } catch (e: Exception) {
            synchronizationState.value = SynchronizationState.Error(e.message ?: "Unknown error")
        }
    }
}