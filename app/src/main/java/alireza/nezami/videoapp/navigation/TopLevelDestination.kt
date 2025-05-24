package alireza.nezami.videoapp.navigation

import alireza.nezami.designsystem.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.home,
        titleTextId = R.string.home
    ),
    SEARCH(
        selectedIcon = Icons.Outlined.Search,
        iconTextId = R.string.search,
        titleTextId = R.string.search,
    ),
    BOOKMARK(
        selectedIcon = Icons.Outlined.Favorite,
        iconTextId = R.string.bookmark,
        titleTextId = R.string.bookmark,
    ),
}
