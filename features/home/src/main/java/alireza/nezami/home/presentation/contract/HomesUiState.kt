package alireza.nezami.home.presentation.contract

import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.domain.VideoState
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class HomesUiState(
        val latestVideos: List<VideoHitDM> = emptyList(),
        val popularVideos: List<VideoHitDM> = emptyList(),

        val latestVideoState: VideoState = VideoState(),
        val popularVideoState: VideoState = VideoState(),

        val selectedTabIndex: Int = HomeTabState.Latest.index,

        ) : Parcelable {

    sealed class PartialState {
        data class AddLatestVideos(val videos: List<VideoHitDM>) : PartialState()
        data class LatestError(val message: String) : PartialState()
        data class LatestLoading(val show: Boolean) : PartialState()

        data class AddPopularVideos(val videos: List<VideoHitDM>) : PartialState()
        data class PopularError(val message: String) : PartialState()
        data class PopularLoading(val show: Boolean) : PartialState()

        data class ChangeTab(val selectedTabIndex: Int) : PartialState()

    }
}
