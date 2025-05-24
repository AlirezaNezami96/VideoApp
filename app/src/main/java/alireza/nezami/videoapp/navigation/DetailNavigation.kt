package alireza.nezami.videoapp.navigation

import alireza.nezami.common.utils.UriDecoder
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

internal const val videoIdArg = "videoId"

internal class DetailArgs(val videoId: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        UriDecoder().decodeString(
            checkNotNull(
                savedStateHandle[videoIdArg]
            )
        )
    )
}

fun NavController.navigateToDetail(videoId: Int) {
    val encodedId = Uri.encode(videoId.toString())
    this.navigate("detail_route/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.detailScreen(
        navigateUp: () -> Unit, onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = "detail_route/{$videoIdArg}", arguments = listOf(
            navArgument(videoIdArg) { type = NavType.StringType },
        )
    ) { //todo: add
    }
}
