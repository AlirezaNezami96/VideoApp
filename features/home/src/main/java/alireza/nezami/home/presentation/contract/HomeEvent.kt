package alireza.nezami.home.presentation.contract


sealed class HomeEvent {
   data object NavigateToSearch : HomeEvent()
    data class NavigateToVideoDetail(val id: Int) : HomeEvent()
}
