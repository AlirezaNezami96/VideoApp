package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.VideoRepository
import alireza.nezami.model.domain.VideoHitDM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetVideoByIdUseCase @Inject constructor(
        private val repository: VideoRepository
) {
    suspend operator fun invoke(id: Int): Flow<List<VideoHitDM>> =
        repository.getVideoById(id).map { videoHitDto ->
            videoHitDto.map { it.toDM() }
        }
}