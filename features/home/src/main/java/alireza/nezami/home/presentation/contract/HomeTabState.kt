package alireza.nezami.home.presentation.contract

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class HomeTabState(val index: Int) : Parcelable {

    @Parcelize
    data object Popular : HomeTabState(0)

    @Parcelize
    data object Latest : HomeTabState(1)

}