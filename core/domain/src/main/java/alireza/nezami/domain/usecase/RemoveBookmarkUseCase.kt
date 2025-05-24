package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.BookmarkRepository
import javax.inject.Inject

class RemoveBookmarkUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.removeBookmark(id)
    }
}