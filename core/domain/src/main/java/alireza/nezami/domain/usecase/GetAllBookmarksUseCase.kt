package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.BookmarkRepository
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.entity.toDM
import javax.inject.Inject

class GetAllBookmarksUseCase @Inject constructor(
        private val repository: BookmarkRepository
) {
    suspend operator fun invoke(): List<VideoHitDM> = repository.getAllBookmarks().map { it.toDM() }
}