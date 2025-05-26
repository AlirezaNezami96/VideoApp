package alireza.nezami.bookmark.presentation.contract

import alireza.nezami.model.domain.VideoHitDM


sealed class BookmarkEvent {
    data class NavigateToVideoDetail(val video: VideoHitDM) : BookmarkEvent()
}
