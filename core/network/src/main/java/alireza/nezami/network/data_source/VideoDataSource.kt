package alireza.nezami.network.data_source

import alireza.nezami.model.data.VideoResponseDto
import kotlinx.coroutines.flow.Flow

interface VideoDataSource {
    suspend fun getLatestVideos(page: Int?, perPage: Int?): Flow<VideoResponseDto>
    suspend fun getPopularVideos(page: Int?, perPage: Int?): Flow<VideoResponseDto>
    suspend fun getVideoById(id: Int): Flow<VideoResponseDto>
    suspend fun searchVideo(
        query: String,
        page: Int?,
        perPage: Int?,
        order: String?
    ): Flow<VideoResponseDto>
}