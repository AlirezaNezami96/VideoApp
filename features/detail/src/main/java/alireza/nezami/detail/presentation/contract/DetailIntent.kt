package alireza.nezami.detail.presentation.contract

sealed class DetailIntent {
    data class GetVideoDetail(val id: Int) : DetailIntent()
    data class GetIsVideoBookmarked(val id: Int) : DetailIntent()
    data object OnBookmarkClick : DetailIntent()
    data object OnNavigateBackClick : DetailIntent()
}
