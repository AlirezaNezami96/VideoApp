package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.BookmarkRepository
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.entity.toDM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBookmarkByIdUseCase @Inject constructor(
        private val repository: BookmarkRepository
) {
    suspend operator fun invoke(id: Int): Flow<VideoHitDM> =
        repository.getBookmarkById(id).map { entity ->
                entity?.toDM() ?: throw NoSuchElementException("Bookmark with id $id not found")
            }
}