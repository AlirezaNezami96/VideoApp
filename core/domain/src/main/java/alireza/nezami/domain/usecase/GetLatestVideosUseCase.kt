package alireza.nezami.domain.usecase

import alireza.nezami.data.repository.VideoRepository
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.entity.toDM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLatestVideosUseCase @Inject constructor(
        private val repository: VideoRepository
) {
    suspend operator fun invoke(page: Int? = null, perPage: Int? = null): Flow<List<VideoHitDM>> =
        repository.getLatestVideos(page, perPage).map { entities ->
            entities.map { it.toDM() }
        }
}