package alireza.nezami.data.repository

import alireza.nezami.data.util.LatestVideoSynchronizer
import alireza.nezami.data.util.SynchronizationState
import alireza.nezami.data.util.PopularVideoSynchronizer
import alireza.nezami.domain.repository.VideoRepository
import alireza.nezami.model.entity.VideoEntity
import alireza.nezami.model.data.VideoHitDto
import alireza.nezami.network.data_source.VideoDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
        private val popularVideoSynchronizer: PopularVideoSynchronizer,
        private val latestVideoSynchronizer: LatestVideoSynchronizer,
        private val remoteDataSource: VideoDataSource
) : VideoRepository {
    override suspend fun getPopularVideos(
            page: Int?, perPage: Int?
    ): Flow<List<VideoEntity>> = flow {
        val stateFlow = popularVideoSynchronizer.synchronizationStateFlow

        stateFlow.collect { state ->
            when (state) {
                is SynchronizationState.Success<*> -> {
                    (state.data as? List<VideoEntity>)?.let {
                        emit(it)
                    }
                }

                is SynchronizationState.Error -> {
                    throw Exception(state.message)
                }

                else -> Unit
            }
        }
    }.onStart {
        popularVideoSynchronizer.startSynchronization()
    }.onCompletion {
        popularVideoSynchronizer.stopSynchronization()
    }

    override suspend fun getLatestVideos(
            page: Int?, perPage: Int?
    ): Flow<List<VideoEntity>> = flow {
        val stateFlow = latestVideoSynchronizer.synchronizationStateFlow

        stateFlow.collect { state ->
            when (state) {
                is SynchronizationState.Success<*> -> {
                    (state.data as? List<VideoEntity>)?.let {
                        emit(it)
                    }
                }

                is SynchronizationState.Error -> {
                    throw Exception(state.message)
                }

                else -> Unit
            }
        }
    }.onStart {
        latestVideoSynchronizer.startSynchronization()
    }.onCompletion {
        latestVideoSynchronizer.stopSynchronization()
    }

    override suspend fun searchVideo(
            query: String, page: Int?, perPage: Int?, order: String?
    ): Flow<List<VideoHitDto>> = remoteDataSource.searchVideo(query, page, perPage, order)
        .map { response -> response.hits.orEmpty() }

    override suspend fun getVideoById(id: Int): Flow<List<VideoHitDto>> =
        remoteDataSource.getVideoById(id).map { response -> response.hits.orEmpty() }
}

