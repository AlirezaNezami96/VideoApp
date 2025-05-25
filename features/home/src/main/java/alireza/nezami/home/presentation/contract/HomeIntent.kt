package alireza.nezami.home.presentation.contract

import alireza.nezami.model.domain.VideoHitDM

sealed class HomeIntent {
    data class GetPopularVideos(val page: Int? = null) : HomeIntent()
    data class GetLatestVideos(val page: Int? = null) : HomeIntent()
    data class OnVideoClick(val id: Int) : HomeIntent()
    data class OnBookmarkClick(val video: VideoHitDM) : HomeIntent()
    data class ChangeTab(val selectedTabIndex: Int) : HomeIntent()
    data object OnSearchClick : HomeIntent()
}
