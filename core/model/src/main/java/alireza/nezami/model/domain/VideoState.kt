package alireza.nezami.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoState(
    val state: ListState = ListState.LOADING,
    val page: Int = 1,
    val errorMessage: String = ""
) : Parcelable {
    val nextPage: Int
        get() = page + 1
}

enum class ListState {
    IDLE,
    LOADING,
    PAGINATING,
    ERROR,
    PAGINATION_EXHAUST,
    EMPTY
}
