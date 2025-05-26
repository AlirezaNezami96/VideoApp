package alireza.nezami.detail.presentation

import alireza.nezami.common.utils.extensions.toAbbreviatedString
import alireza.nezami.common.utils.extensions.toTagList
import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.component.DynamicAsyncImage
import alireza.nezami.designsystem.component.GradientColorProvider
import alireza.nezami.designsystem.component.HeightSpacer
import alireza.nezami.designsystem.component.ThumbnailSelector
import alireza.nezami.designsystem.component.TopAppBar
import alireza.nezami.designsystem.component.WidthSpacer
import alireza.nezami.designsystem.component.gradientBorder
import alireza.nezami.designsystem.extensions.collectWithLifecycle
import alireza.nezami.detail.presentation.contract.DetailEvent
import alireza.nezami.detail.presentation.contract.DetailIntent
import alireza.nezami.detail.presentation.contract.DetailUiState
import alireza.nezami.detail.presentation.contract.VideoPlayerState
import alireza.nezami.model.domain.VideoHitDM
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.flow.Flow

@Composable
fun DetailScreen(
        viewModel: DetailViewModel = hiltViewModel(), navigateUp: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    HandleEvents(
        events = viewModel.event, navigateUp = navigateUp
    )

    DetailContent(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
        updateVideoPlayerState = viewModel::updateVideoPlayerState,
        videoPlayerState = viewModel.videoPlayerState.value,
        getOrCreatePlayer = viewModel::getOrCreatePlayer
    )
}

@Composable
fun DetailContent(
        uiState: DetailUiState,
        onIntent: (DetailIntent) -> Unit,
        updateVideoPlayerState: (VideoPlayerState) -> Unit,
        getOrCreatePlayer: (String) -> ExoPlayer,
        videoPlayerState: VideoPlayerState
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        topBar(uiState, onIntent)
        loading(uiState.isLoading)
        videoPlayer(
            uiState.video,
            updateVideoPlayerState = updateVideoPlayerState,
            videoPlayerState = videoPlayerState,
            getOrCreatePlayer = getOrCreatePlayer
        )
        statisticsLayout(uiState.video)
        userParts(uiState.video)

        tagsList(uiState.video)
    }

}

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.statisticsLayout(video: VideoHitDM?) {
    item {
        HeightSpacer(8)
        FlowRow(
            horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
        ) {
            StatisticText(
                text = video?.views.toAbbreviatedString(),
                icon = R.drawable.ic_view,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            StatisticText(
                text = video?.downloads.toAbbreviatedString(),
                icon = R.drawable.ic_download,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            StatisticText(
                text = video?.likes.toAbbreviatedString(),
                icon = R.drawable.ic_like,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            StatisticText(
                text = video?.comments.toAbbreviatedString(),
                icon = R.drawable.ic_comment,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
fun StatisticText(
        text: String, @DrawableRes icon: Int, modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .background(Color.Transparent)
            .padding(horizontal = 16.dp)
    ) {
        WidthSpacer(12)
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .size(20.dp)
        )
        WidthSpacer(8)
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 10.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        WidthSpacer(16)
    }
}

@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.tagsList(video: VideoHitDM?) {
    item {
        HeightSpacer(16)
    }
    item {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Tags: ", style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ), modifier = Modifier.align(Alignment.CenterVertically)
            )

            video?.tags?.toTagList().orEmpty().forEach { tag ->
                Text(
                    text = tag,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.Transparent)
                        .gradientBorder(
                            width = (0.6).dp,
                            gradient = GradientColorProvider.getRandomGradient(),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }

}

fun LazyListScope.userParts(video: VideoHitDM?) {

    item {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp),
        ) {
            DynamicAsyncImage(
                imageUrl = video?.userImageURL.orEmpty(),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )

            WidthSpacer(16)
            Text(
                text = video?.user.orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium
                )
            )


        }
    }
    item {
        HeightSpacer(8)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun LazyListScope.topBar(uiState: DetailUiState, onIntent: (DetailIntent) -> Unit) {
    item {
        TopAppBar(
            titleRes = R.string.detail,
            navigationIcon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
            onNavigationClick = {
                onIntent.invoke(DetailIntent.OnNavigateBackClick)
            },
            actionIcon = if (uiState.isBookmarked) R.drawable.ic_bookmark_filled
            else R.drawable.ic_bookmark_stroke,
            onActionClick = {
                onIntent.invoke(DetailIntent.OnBookmarkClick)
            })
    }
}

fun LazyListScope.loading(loading: Boolean) {
    item {
        if (loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}


fun LazyListScope.videoPlayer(
        video: VideoHitDM?,
        getOrCreatePlayer: (String) -> ExoPlayer,
        updateVideoPlayerState: (VideoPlayerState) -> Unit,
        videoPlayerState: VideoPlayerState
) {
    item {
        val thumbnailHeight = ThumbnailSelector.calculateThumbnailHeight(video?.videos)
        val thumbnail = ThumbnailSelector.selectThumbnail(video?.videos).url
        var isPlaying by rememberSaveable { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isPlaying) {
                video?.videos?.large?.url?.let { videoUrl ->
                    VideoPlayer(
                        url = videoUrl,
                        videoPlayerState = videoPlayerState,
                        modifier = Modifier
                            .height(thumbnailHeight)
                            .fillMaxWidth(),
                        getOrCreatePlayer = getOrCreatePlayer,
                        updateVideoPlayerState = updateVideoPlayerState,
                        autoPlay = true
                    )
                }
            } else {
                DynamicAsyncImage(
                    contentDescription = "Video Poster",
                    modifier = Modifier
                        .height(thumbnailHeight)
                        .fillMaxWidth(),
                    imageUrl = thumbnail
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(100.dp)
                        .height(64.dp)
                        .background(
                            Color.Black.copy(alpha = 0.7f), MaterialTheme.shapes.large
                        )
                        .clickable { isPlaying = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play_video),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

private val VideoPlayerStateSaver =
    Saver<MutableState<VideoPlayerState>, List<Any>>(save = { state ->
        listOf(
            state.value.isFullScreen, state.value.isPlaying, state.value.playbackPosition
        )
    }, restore = { savedState ->
        mutableStateOf(
            VideoPlayerState(
                isFullScreen = savedState[0] as Boolean,
                isPlaying = savedState[1] as Boolean,
                playbackPosition = savedState[2] as Long
            )
        )
    })

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
        url: String,
        modifier: Modifier = Modifier,
        getOrCreatePlayer: (String) -> ExoPlayer,
        videoPlayerState: VideoPlayerState,
        autoPlay: Boolean = false,
        updateVideoPlayerState: (VideoPlayerState) -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val playerState = rememberSaveable(stateSaver = VideoPlayerStateSaver) {
        mutableStateOf(mutableStateOf(videoPlayerState))
    }

    val exoPlayer = remember(url) {
        getOrCreatePlayer(url).apply {
            if (autoPlay) {
                playWhenReady = true
                prepare()
                play()
            }
        }
    }

    DisposableEffect(playerState.value) {
        updateVideoPlayerState(playerState.value.value)
        onDispose { }
    }

    val playerView = remember {
        PlayerView(context).apply {
            player = exoPlayer
            useController = true
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            setShowNextButton(false)
            setShowPreviousButton(false)
        }
    }

    DisposableEffect(key1 = Unit) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    playerState.value.value = playerState.value.value.copy(
                        isPlaying = exoPlayer.isPlaying,
                        playbackPosition = exoPlayer.currentPosition
                    )
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    if (playerState.value.value.isPlaying) {
                        exoPlayer.play()
                    }
                }

                else -> {}
            }
        }

        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            playerState.value.value = playerState.value.value.copy(
                isPlaying = exoPlayer.isPlaying, playbackPosition = exoPlayer.currentPosition
            )
        }
    }

    Box(
        modifier = if (playerState.value.value.isFullScreen) {
            Modifier.fillMaxSize()
        } else {
            modifier
        }
    ) {
        AndroidView(
            factory = { playerView }, modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
private fun HandleEvents(
        events: Flow<DetailEvent>, navigateUp: () -> Unit
) {
    events.collectWithLifecycle {
        when (it) {
            DetailEvent.NavigateBack -> navigateUp()
        }
    }
}

