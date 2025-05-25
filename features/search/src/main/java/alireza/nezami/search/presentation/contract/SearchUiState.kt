package alireza.nezami.search.presentation.contract

import alireza.nezami.model.domain.ListState
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.domain.VideoState
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import javax.annotation.concurrent.Immutable

@Immutable
@Parcelize
data class SearchUiState(
        val searchQuery: String = "",
        val searchResultList: List<VideoHitDM> = emptyList(),
        val searchResultState: VideoState = VideoState(state = ListState.IDLE)
) : Parcelable {

    sealed class PartialState {
        data class AddSearchResult(val videos: List<VideoHitDM>) : PartialState()
        data class EnterSearchQuery(val query: String) : PartialState()
        data class SearchResultError(val message: String) : PartialState()
        data class SearchResultLoading(val show: Boolean) : PartialState()

        data class UpdateBookmark(val video: VideoHitDM) : PartialState()

    }
}
