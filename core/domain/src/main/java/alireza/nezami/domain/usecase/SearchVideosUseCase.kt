package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.VideoRepository
import alireza.nezami.model.domain.VideoHitDM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchVideosUseCase @Inject constructor(
        private val repository: VideoRepository
) {
    suspend operator fun invoke(
            query: String,
            page: Int? = null,
            perPage: Int? = null,
            order: String? = null,
            filterByDuration: Boolean = true
    ): Flow<List<VideoHitDM>> =
        repository.searchVideo(query, page, 100, order).map { dto ->
            dto
                .map { it.toDM() }
                .let { videos ->
                    if (filterByDuration) {
                        videos.filter { it.duration >= 60 }
                    } else videos
                }
        }
}