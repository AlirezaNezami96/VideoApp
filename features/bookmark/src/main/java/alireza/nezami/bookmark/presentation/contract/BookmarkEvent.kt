package alireza.nezami.bookmark.presentation.contract


sealed class BookmarkEvent {
    data class NavigateToVideoDetail(val id: Int) : BookmarkEvent()
}
