package alireza.nezami.search.presentation.contract

import alireza.nezami.model.domain.VideoHitDM

sealed class SearchIntent {
    data class EnterSearchQuery(val query: String) : SearchIntent()
    data object Paginate : SearchIntent()
    data class OnVideoClick(val video:VideoHitDM) : SearchIntent()
    data class OnBookmarkClick(val video: VideoHitDM) : SearchIntent()

}
