package alireza.nezami.bookmark.presentation.contract

import alireza.nezami.model.domain.VideoHitDM

sealed class BookmarkIntent {
    data object GetBookmarkVideos : BookmarkIntent()
    data class OnVideoClick(val id: Int) : BookmarkIntent()
    data class OnBookmarkClick(val video: VideoHitDM) : BookmarkIntent()
}
