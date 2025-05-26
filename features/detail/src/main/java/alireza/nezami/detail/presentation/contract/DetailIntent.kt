package alireza.nezami.detail.presentation.contract

import alireza.nezami.model.domain.VideoHitDM

sealed class DetailIntent {
    data class GetVideoDetail(val video: VideoHitDM?) : DetailIntent()
    data class GetIsVideoBookmarked(val id: Int) : DetailIntent()
    data object OnBookmarkClick : DetailIntent()
    data object OnNavigateBackClick : DetailIntent()
}
