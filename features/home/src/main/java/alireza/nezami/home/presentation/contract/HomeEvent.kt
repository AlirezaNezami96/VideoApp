package alireza.nezami.home.presentation.contract

import alireza.nezami.model.domain.VideoHitDM


sealed class HomeEvent {
   data object NavigateToSearch : HomeEvent()
    data class NavigateToVideoDetail(val video: VideoHitDM) : HomeEvent()
}
