package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.VideoRepository
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.entity.toDM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPopularVideosUseCase @Inject constructor(
        private val repository: VideoRepository
) {
    suspend operator fun invoke(
            page: Int? = null,
            perPage: Int? = null,
            filterByDuration: Boolean = false
    ): Flow<List<VideoHitDM>> =
        repository.getPopularVideos(page, perPage).map { entities ->
            entities
                .map { it.toDM() }
                .let { videos ->
                    if (filterByDuration) {
                        videos.filter { it.duration >= 60 }
                    } else videos
                }
        }
}
