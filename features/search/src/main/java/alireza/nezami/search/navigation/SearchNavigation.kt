package alireza.nezami.search.navigation

import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.search.presentation.SearchScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val searchNavigationRoute = "search_route"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(searchNavigationRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
        onVideoClick: (video: VideoHitDM) -> Unit, onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = searchNavigationRoute,
    ) {
        SearchScreen(
            onVideoClick = onVideoClick,
        )
    }
}
