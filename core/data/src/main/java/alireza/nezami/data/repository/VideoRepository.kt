package alireza.nezami.data.repository

import alireza.nezami.model.entity.VideoEntity
import alireza.nezami.model.data.VideoHitDto
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    suspend fun getPopularVideos(page: Int? = null, perPage: Int? = null): Flow<List<VideoEntity>>
    suspend fun getLatestVideos(page: Int? = null, perPage: Int? = null): Flow<List<VideoEntity>>
    suspend fun searchVideo(
        query: String,
        page: Int? = null,
        perPage: Int? = null,
        order: String? = null
    ): Flow<List<VideoHitDto>>
    suspend fun getVideoById(id: Int): Flow<List<VideoHitDto>>
}