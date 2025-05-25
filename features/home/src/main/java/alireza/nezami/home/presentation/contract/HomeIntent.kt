package alireza.nezami.home.presentation.contract

sealed class HomeIntent {
    data class GetPopular(val page: Int? = null) : HomeIntent()
    data class GetLatest(val page: Int? = null) : HomeIntent()
    data class OnVideoClick(val id: Int) : HomeIntent()
    data class ChangeTab(val selectedTabIndex: Int) : HomeIntent()
    data object OnSearchClick : HomeIntent()
}
