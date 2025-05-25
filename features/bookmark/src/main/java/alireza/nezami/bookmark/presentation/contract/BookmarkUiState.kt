package alireza.nezami.bookmark.presentation.contract

import alireza.nezami.model.domain.ListState
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.domain.VideoState
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import javax.annotation.concurrent.Immutable

@Immutable
@Parcelize
data class BookmarkUiState(
        val bookmarkVideoList: List<VideoHitDM> = emptyList(),
        val bookmarkListState: VideoState = VideoState(state = ListState.LOADING)
) : Parcelable {

    sealed class PartialState {
        data class AddBookmarkVideoList(val videos: List<VideoHitDM>) : PartialState()
        data class Error(val errorMessage: String) : PartialState()
        data class Loading(val shown: Boolean) : PartialState()

        data class UpdateBookmark(val video: VideoHitDM) : PartialState()
    }
}
