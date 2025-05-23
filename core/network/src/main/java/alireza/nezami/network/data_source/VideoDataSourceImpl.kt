package alireza.nezami.network.data_source

import alireza.nezami.model.data.VideoResponseDto
import alireza.nezami.network.api_service.PixabayVideoApiService
import alireza.nezami.network.model.ApiPageConfig
import alireza.nezami.network.model.VideoOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoDataSourceImpl @Inject constructor(
        private val apiService: PixabayVideoApiService
) : VideoDataSource {

    override suspend fun getLatestVideos(page: Int?, perPage: Int?): Flow<VideoResponseDto> = flow {
        emit(
            apiService.getLatestVideos(
                page = page ?: ApiPageConfig.DEFAULT_PAGE,
                perPage = perPage ?: ApiPageConfig.DEFAULT_PER_PAGE
            )
        )
    }

    override suspend fun getPopularVideos(page: Int?, perPage: Int?): Flow<VideoResponseDto> =
        flow {
            emit(
                apiService.getPopularVideos(
                    page = page ?: ApiPageConfig.DEFAULT_PAGE,
                    perPage = perPage ?: ApiPageConfig.DEFAULT_PER_PAGE
                )
            )
        }

    override suspend fun getVideoById(id: Int): Flow<VideoResponseDto> = flow {
        emit(apiService.getVideoById(id = id))
    }

    override suspend fun searchVideo(
            query: String, page: Int?, perPage: Int?, order: String?
    ): Flow<VideoResponseDto> = flow {
        emit(
            apiService.searchVideo(
                query = query,
                page = page ?: ApiPageConfig.DEFAULT_PAGE,
                perPage = perPage ?: ApiPageConfig.DEFAULT_PER_PAGE,
                order = order ?: VideoOrder.DEFAULT.value
            )
        )
    }
}