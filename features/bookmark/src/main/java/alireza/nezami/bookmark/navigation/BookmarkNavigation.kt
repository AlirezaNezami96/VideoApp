package alireza.nezami.bookmark.navigation

import alireza.nezami.bookmark.presentation.BookmarkScreen
import alireza.nezami.model.domain.VideoHitDM
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val bookmarkNavigationRoute = "bookmark_route"

fun NavController.navigateToBookmark(navOptions: NavOptions? = null) {
    this.navigate(bookmarkNavigationRoute, navOptions)
}

fun NavGraphBuilder.bookmarkScreen(
        onVideoClick: (video: VideoHitDM) -> Unit, onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        route = bookmarkNavigationRoute,
    ) {
        BookmarkScreen(
            onVideoClick = onVideoClick
        )
    }
}
