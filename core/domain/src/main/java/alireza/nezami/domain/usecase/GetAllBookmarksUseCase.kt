package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.BookmarkRepository
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.entity.toDM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllBookmarksUseCase @Inject constructor(
        private val repository: BookmarkRepository
) {
    suspend operator fun invoke(): Flow<List<VideoHitDM>> =
        repository.getAllBookmarks().map { entities ->
            entities.map { it.toDM() }
        }
}