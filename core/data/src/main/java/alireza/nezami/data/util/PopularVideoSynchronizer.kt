package alireza.nezami.data.util

import alireza.nezami.database.dao.BookmarkDao
import alireza.nezami.database.dao.VideoDao
import alireza.nezami.model.data.ApiPageConfig
import alireza.nezami.model.data.VideoOrder
import alireza.nezami.model.entity.VideoEntity
import alireza.nezami.network.data_source.VideoDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PopularVideoSynchronizer @Inject constructor(
        private val localVideoDataSource: VideoDao,
        private val localBookmarkDataSource: BookmarkDao,
        private val remoteDataSource: VideoDataSource,
        private val coroutineScope: CoroutineScope,
        private val mapper: VideoMapper,
) {
    private val _synchronizationState = MutableStateFlow<SynchronizationState>(SynchronizationState.Idle)
    val synchronizationStateFlow: StateFlow<SynchronizationState> get() = _synchronizationState

    fun startSynchronization(page: Int? = null, perPage: Int? = null) {
        coroutineScope.launch {
            _synchronizationState.value = SynchronizationState.Idle
            synchronize(
                page = page ?: ApiPageConfig.DEFAULT_PAGE,
                perPage = perPage ?: ApiPageConfig.DEFAULT_PER_PAGE
            )
        }
    }

    fun stopSynchronization() {
        _synchronizationState.value = SynchronizationState.Idle
        coroutineScope.cancel()
    }

    private suspend fun synchronize(
            page: Int = ApiPageConfig.DEFAULT_PAGE, perPage: Int = ApiPageConfig.DEFAULT_PER_PAGE
    ) {
        _synchronizationState.value = SynchronizationState.InProgress

        try {
            remoteDataSource.getPopularVideos(page = page, perPage = perPage)
                .catch { // If remote fails, get from local
                    val localVideos = localVideoDataSource.getAllPopularVideos().first()
                    if (localVideos.isEmpty()) {
                        _synchronizationState.value = SynchronizationState.Error("No data available")
                    } else {
                        emitVideosWithBookmarks(localVideos)
                    }
                }.collect { remoteData ->
                    if (remoteData.hits?.isNotEmpty() == true) {
                        val mappedData = mapper.mapToEntities(
                            videoResponses = remoteData.hits.orEmpty(), order = VideoOrder.POPULAR
                        )

                        // Save to local DB
                        localVideoDataSource.deleteVideosByType(VideoOrder.POPULAR.value)
                        localVideoDataSource.insertVideos(mappedData)

                        emitVideosWithBookmarks(mappedData)
                    } else {
                        _synchronizationState.value = SynchronizationState.Error("No data available")
                    }
                }
        } catch (e: Exception) {
            _synchronizationState.value = SynchronizationState.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun emitVideosWithBookmarks(videos: List<VideoEntity>) {
        val bookmarkedIds = localBookmarkDataSource.getAllBookmarks().first().map { it.id }

        val videosWithBookmarks = videos.map { video ->
            video.copy(isBookmarked = bookmarkedIds.contains(video.id))
        }

        _synchronizationState.value = SynchronizationState.Success(videosWithBookmarks)
        Timber.d("PopularVideoSynchronizer: Synchronization successful with ${videosWithBookmarks.size} videos")
    }
}