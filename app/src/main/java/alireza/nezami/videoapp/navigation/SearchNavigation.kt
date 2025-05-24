package alireza.nezami.videoapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val searchNavigationRoute = "search_route"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(searchNavigationRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
        onVideoClick: (Int) -> Unit,
        onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = searchNavigationRoute,
    ) { //todo: add
    }
}
