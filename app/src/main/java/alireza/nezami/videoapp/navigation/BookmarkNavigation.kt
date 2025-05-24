package alireza.nezami.videoapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val bookmarkNavigationRoute = "bookmark_route"

fun NavController.navigateToBookmark(navOptions: NavOptions? = null) {
    this.navigate(bookmarkNavigationRoute, navOptions)
}

fun NavGraphBuilder.bookmarkScreen(
        onVideoClick: (Int) -> Unit,
        onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = bookmarkNavigationRoute,
    ) {
        //todo: add
    }
}
