package alireza.nezami.videoapp.navigation

import alireza.nezami.videoapp.ui.AppState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost

/**
 * Top-level navigation graph.
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun AppNavHost(
        appState: AppState,
        onShowSnackbar: suspend (String, String?) -> Boolean,
        modifier: Modifier = Modifier,
        startDestination: String = homeNavigationRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(onVideoClick = navController::navigateToDetail,
            onShowSnackbar = onShowSnackbar,
            onSearchClick = {
                navController.navigateToSearch()
            })

        searchScreen(
            onVideoClick = navController::navigateToDetail, onShowSnackbar = onShowSnackbar
        )

        bookmarkScreen(
            onVideoClick = navController::navigateToDetail, onShowSnackbar = onShowSnackbar
        )

        detailScreen(
            navigateUp = navController::navigateUp, onShowSnackbar = onShowSnackbar
        )

    }
}

