package alireza.nezami.search.presentation.contract

import alireza.nezami.model.domain.VideoHitDM


sealed class SearchEvent {
    data class NavigateToVideoDetail(val video: VideoHitDM) : SearchEvent()
}
