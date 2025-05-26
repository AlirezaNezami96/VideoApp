package alireza.nezami.home.presentation

import alireza.nezami.common.utils.extensions.toTagList
import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.component.HeightSpacer
import alireza.nezami.designsystem.component.ScrollableTabRow
import alireza.nezami.designsystem.component.SearchInput
import alireza.nezami.designsystem.component.Tab
import alireza.nezami.designsystem.component.VideoCard
import alireza.nezami.designsystem.extensions.collectWithLifecycle
import alireza.nezami.home.presentation.contract.HomeEvent
import alireza.nezami.home.presentation.contract.HomeIntent
import alireza.nezami.home.presentation.contract.HomeTabState
import alireza.nezami.home.presentation.contract.HomesUiState
import alireza.nezami.model.domain.ListState
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.domain.VideoState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
        viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
        onVideoClick: (video: VideoHitDM) -> Unit,
        onSearchClick: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    HandleEvents(
        events = viewModel.event,
        navigateToVideoDetail = onVideoClick,
        navigateToSearch = onSearchClick
    )

    HomeContent(
        uiState = uiState, onIntent = viewModel::acceptIntent
    )
}

@Composable
fun HomeContent(uiState: HomesUiState, onIntent: (HomeIntent) -> Unit) {
    Column(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxSize()
            .padding(
                horizontal = 16.dp,
            )
    ) {
        SearchContent(onIntent)

        TabContent(uiState, onIntent)

        VideoListContent(
            shown = uiState.selectedTabIndex == HomeTabState.Popular.index,
            listState = uiState.popularVideoState,
            list = uiState.popularVideos,
            onPaginate = {
                onIntent.invoke(HomeIntent.GetPopularVideos())
            },
            onBookmarkClick = {
                onIntent.invoke(HomeIntent.OnBookmarkClick(it))
            },
            onVideoCardClick = {
                onIntent.invoke(HomeIntent.OnVideoClick(it))
            })
        VideoListContent(
            shown = uiState.selectedTabIndex == HomeTabState.Latest.index,
            listState = uiState.latestVideoState,
            list = uiState.latestVideos,
            onPaginate = {
                onIntent.invoke(HomeIntent.GetLatestVideos())
            },
            onVideoCardClick = {
                onIntent.invoke(HomeIntent.OnVideoClick(it))
            },
            onBookmarkClick = {
                onIntent.invoke(HomeIntent.OnBookmarkClick(it))
            },
        )

    }
}

@Composable
fun LoadingComponent() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun VideoListContent(
        listState: VideoState,
        list: List<VideoHitDM>,
        onPaginate: () -> Unit,
        onBookmarkClick: (video: VideoHitDM) -> Unit,
        onVideoCardClick: (video: VideoHitDM) -> Unit,
        shown: Boolean
) {
    if (shown) {
        val lazyListThreshold = 6
        val lazyListState = rememberLazyListState()

        val shouldStartPaginate = remember {
            derivedStateOf {
                (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    ?: -9) >= (lazyListState.layoutInfo.totalItemsCount - lazyListThreshold)
            }
        }

        LaunchedEffect(key1 = shouldStartPaginate.value) {
            if (shouldStartPaginate.value && listState.state == ListState.IDLE) onPaginate()
        }



        LazyColumn(
            state = lazyListState, contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = list,
            ) { video ->
                VideoCard(
                    userName = video.user,
                    userAvatar = video.userImageURL,
                    isBookmarked = video.isBookmarked,
                    tagsList = video.tags.toTagList(),
                    modifier = Modifier.padding(vertical = 8.dp),
                    videos = video.videos,
                    onVideoCardClick = {
                        onVideoCardClick(video)
                    },
                    onBookmarkClick = {
                        onBookmarkClick(video)
                    })
            }

            item(
                key = listState,
            ) {
                when (listState.state) {
                    ListState.LOADING, ListState.PAGINATING -> LoadingComponent()

                    ListState.ERROR -> PaginationErrorText(listState.errorMessage)
                    ListState.PAGINATION_EXHAUST -> PaginationErrorText(stringResource(R.string.nothing_left_to_show))
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun PaginationErrorText(text: String) {
    Box(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.medium
            )
            .border(
                width = 1.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primary
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}

@Composable
fun SearchContent(onIntent: (HomeIntent) -> Unit) {
    val focusRequester = remember { FocusRequester() }

    SearchInput(
        focusRequester = focusRequester, onParentClick = {
            onIntent.invoke(HomeIntent.OnSearchClick)
        })
}

@Composable
private fun TabContent(uiState: HomesUiState, onIntent: (HomeIntent) -> Unit) {
    HeightSpacer(value = 8)

    val titles = listOf(
        R.string.popular,
        R.string.latest,
    )
    ScrollableTabRow(selectedTabIndex = uiState.selectedTabIndex) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = uiState.selectedTabIndex == index,
                onClick = { onIntent.invoke(HomeIntent.ChangeTab(index)) },
                text = {
                    Text(text = stringResource(title))
                },
            )
        }
    }
}

@Composable
private fun HandleEvents(
        events: Flow<HomeEvent>,
        navigateToVideoDetail: (video: VideoHitDM) -> Unit,
        navigateToSearch: () -> Unit
) {
    events.collectWithLifecycle {
        when (it) {
            HomeEvent.NavigateToSearch -> navigateToSearch()
            is HomeEvent.NavigateToVideoDetail -> navigateToVideoDetail(it.video)
        }
    }
}
