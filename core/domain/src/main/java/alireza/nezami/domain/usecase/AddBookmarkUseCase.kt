package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.BookmarkRepository
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.entity.BookmarkEntity
import javax.inject.Inject

class AddBookmarkUseCase @Inject constructor(
        private val repository: BookmarkRepository
) {
    suspend operator fun invoke(video: VideoHitDM) = repository.addBookmark(
        BookmarkEntity(
            id = video.id,
            pageURL = video.pageURL,
            type = video.type,
            tags = video.tags,
            duration = video.duration,
            videos = video.videos?.toEntity(),
            views = video.views,
            downloads = video.downloads,
            likes = video.likes,
            comments = video.comments,
            userId = video.userId,
            user = video.user,
            userImageURL = video.userImageURL
        )
    )
}