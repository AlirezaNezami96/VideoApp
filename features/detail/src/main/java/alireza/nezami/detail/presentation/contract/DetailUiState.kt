package alireza.nezami.detail.presentation.contract

import alireza.nezami.model.domain.VideoHitDM
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import javax.annotation.concurrent.Immutable

@Immutable
@Parcelize
data class DetailUiState(
        val video: VideoHitDM? = null,

        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isBookmarked: Boolean = false,
) : Parcelable {

    sealed class PartialState {
        data class AddVideoDetail(val detail: VideoHitDM?) : PartialState()
        data class Error(val errorMessage: String) : PartialState()
        data class Loading(val shown: Boolean) : PartialState()
        data class UpdateBookmark(val video: VideoHitDM) : PartialState()
        data class IsBookmarked(val isBookmarked: Boolean) : PartialState()

    }
}
