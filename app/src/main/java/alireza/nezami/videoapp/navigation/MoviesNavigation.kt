package alireza.nezami.videoapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
        onVideoClick: (Int) -> Unit,
        onSearchClick: () -> Unit,
        onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = homeNavigationRoute,
    ) {
        //todo: add
    }
}
