package alireza.nezami.network.data_source

import alireza.nezami.model.data.ApiPageConfig
import alireza.nezami.model.data.VideoOrder
import alireza.nezami.model.data.VideoResponseDto
import alireza.nezami.network.BuildConfig
import alireza.nezami.network.api_service.PixabayVideoApiService
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
                apiKey = BuildConfig.PIXABAY_API_KEY,
                page = page ?: ApiPageConfig.DEFAULT_PAGE,
                perPage = perPage ?: ApiPageConfig.DEFAULT_PER_PAGE
            )
        )
    }

    override suspend fun getPopularVideos(page: Int?, perPage: Int?): Flow<VideoResponseDto> =
        flow {
            emit(
                apiService.getPopularVideos(
                    apiKey = BuildConfig.PIXABAY_API_KEY,
                    page = page ?: ApiPageConfig.DEFAULT_PAGE,
                    perPage = perPage ?: ApiPageConfig.DEFAULT_PER_PAGE
                )
            )
        }

    override suspend fun getVideoById(id: Int): Flow<VideoResponseDto> = flow {
        emit(
            apiService.getVideoById(
                apiKey = BuildConfig.PIXABAY_API_KEY, id = id
            )
        )
    }

    override suspend fun searchVideo(
            query: String, page: Int?, perPage: Int?, order: String?
    ): Flow<VideoResponseDto> = flow {
        emit(
            apiService.searchVideo(
                apiKey = BuildConfig.PIXABAY_API_KEY,
                query = query,
                page = page ?: ApiPageConfig.DEFAULT_PAGE,
                perPage = perPage ?: ApiPageConfig.DEFAULT_PER_PAGE,
                order = order ?: VideoOrder.DEFAULT.value
            )
        )
    }
}