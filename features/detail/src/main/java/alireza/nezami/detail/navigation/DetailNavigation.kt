package alireza.nezami.detail.navigation

import alireza.nezami.common.utils.UriDecoder
import alireza.nezami.detail.presentation.DetailScreen
import alireza.nezami.model.domain.VideoHitDM
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal const val videoArg = "video"

internal class DetailArgs(val video: VideoHitDM) {
    companion object {
        fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): DetailArgs? {
            val videoJson = checkNotNull<String>(savedStateHandle[videoArg]) {
                "Video parameter wasn't found. Please ensure it's passed via navigation."
            }
            val decodedJson = UriDecoder().decodeString(videoJson)
            val video = Json.decodeFromString<VideoHitDM>(decodedJson)
            return DetailArgs(video)
        }
    }
}
fun NavController.navigateToDetail(video: VideoHitDM) {
    val videoJson = Json.encodeToString(video)
    val encodedVideo = android.net.Uri.encode(videoJson)
    this.navigate("detail_route/$encodedVideo") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.detailScreen(
        navigateUp: () -> Unit,
        onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = "detail_route/{$videoArg}",
        arguments = listOf(
            navArgument(videoArg) { type = NavType.StringType },
        )
    ) {
        DetailScreen(
            navigateUp = navigateUp
        )
    }
}