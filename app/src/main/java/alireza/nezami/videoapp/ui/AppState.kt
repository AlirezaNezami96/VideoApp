package alireza.nezami.videoapp.ui

import alireza.nezami.domain.util.NetworkMonitor
import alireza.nezami.home.navigation.homeNavigationRoute
import alireza.nezami.home.navigation.navigateToHome
import alireza.nezami.videoapp.navigation.TopLevelDestination
import alireza.nezami.bookmark.navigation.bookmarkNavigationRoute
import alireza.nezami.bookmark.navigation.navigateToBookmark
import alireza.nezami.search.navigation.navigateToSearch
import alireza.nezami.search.navigation.searchNavigationRoute
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberAppState(
        networkMonitor: NetworkMonitor,
        coroutineScope: CoroutineScope = rememberCoroutineScope(),
        navController: NavHostController = rememberNavController(),
): AppState {
    return remember(
        navController,
        coroutineScope,
        networkMonitor,
    ) {
        AppState(
            navController,
            coroutineScope,
            networkMonitor,
        )
    }
}

@Stable
class AppState(
        val navController: NavHostController,
        coroutineScope: CoroutineScope,
        networkMonitor: NetworkMonitor,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homeNavigationRoute -> TopLevelDestination.HOME
            searchNavigationRoute -> TopLevelDestination.SEARCH
            alireza.nezami.bookmark.navigation.bookmarkNavigationRoute -> TopLevelDestination.BOOKMARK
            else -> null
        }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries


    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.SEARCH -> navController.navigateToSearch(topLevelNavOptions)
            TopLevelDestination.BOOKMARK -> navController.navigateToBookmark(topLevelNavOptions)
        }
    }
}