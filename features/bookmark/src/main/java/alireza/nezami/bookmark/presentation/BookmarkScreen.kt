package alireza.nezami.bookmark.presentation

import alireza.nezami.bookmark.presentation.contract.BookmarkEvent
import alireza.nezami.bookmark.presentation.contract.BookmarkIntent
import alireza.nezami.bookmark.presentation.contract.BookmarkUiState
import alireza.nezami.common.utils.extensions.toTagList
import alireza.nezami.designsystem.component.EmptyBookmarkResult
import alireza.nezami.designsystem.component.VideoCard
import alireza.nezami.designsystem.extensions.collectWithLifecycle
import alireza.nezami.model.domain.ListState
import alireza.nezami.model.domain.VideoHitDM
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun BookmarkScreen(
        viewModel: BookmarkViewModel = hiltViewModel(), onVideoClick: (video: VideoHitDM) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    HandleEvents(events = viewModel.event, navigateToVideoDetail = onVideoClick)

    BookmarkContent(
        uiState = uiState, onIntent = viewModel::acceptIntent
    )
}

@Composable
fun BookmarkContent(uiState: BookmarkUiState, onIntent: (BookmarkIntent) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        when (uiState.bookmarkListState.state) {
            ListState.LOADING -> LoadingComponent()

            ListState.EMPTY, ListState.ERROR -> EmptyBookmarkResult(
                modifier = Modifier.align(Alignment.Center)
            )

            ListState.IDLE -> VideoListContent(
                list = uiState.bookmarkVideoList,
                onVideoCardClick = {
                    onIntent.invoke(BookmarkIntent.OnVideoClick(it))
                },
                onBookmarkClick = {
                    onIntent.invoke(BookmarkIntent.OnBookmarkClick(it))
                },
            )

            else -> {}
        }

    }

}

@Composable
fun BoxScope.LoadingComponent() {
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .size(48.dp)
            .align(Alignment.Center)
    )
}

@Composable
fun VideoListContent(
        list: List<VideoHitDM>,
        onBookmarkClick: (video: VideoHitDM) -> Unit,
        onVideoCardClick: (video: VideoHitDM) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState, contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(
            list
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
    }
}


@Composable
private fun HandleEvents(
        events: Flow<BookmarkEvent>, navigateToVideoDetail: (video: VideoHitDM) -> Unit
) {
    events.collectWithLifecycle {
        when (it) {
            is BookmarkEvent.NavigateToVideoDetail -> navigateToVideoDetail(it.video)
        }
    }
}