package alireza.nezami.search.presentation.contract


sealed class SearchEvent {
    data class NavigateToVideoDetail(val id: Int) : SearchEvent()
}
