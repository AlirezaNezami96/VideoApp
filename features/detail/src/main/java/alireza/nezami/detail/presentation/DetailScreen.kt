package alireza.nezami.detail.presentation

import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.component.DynamicAsyncImage
import alireza.nezami.designsystem.component.ThumbnailSelector
import alireza.nezami.designsystem.component.TopAppBar
import alireza.nezami.designsystem.extensions.collectWithLifecycle
import alireza.nezami.detail.presentation.contract.DetailEvent
import alireza.nezami.detail.presentation.contract.DetailIntent
import alireza.nezami.detail.presentation.contract.DetailUiState
import alireza.nezami.detail.presentation.contract.VideoPlayerState
import alireza.nezami.model.domain.VideoHitDM
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
            onIntent,
            updateVideoPlayerState = updateVideoPlayerState,
            videoPlayerState = videoPlayerState,
            getOrCreatePlayer = getOrCreatePlayer
        )
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
        onIntent: (DetailIntent) -> Unit,
        getOrCreatePlayer: (String) -> ExoPlayer,
        updateVideoPlayerState: (VideoPlayerState) -> Unit,
        videoPlayerState: VideoPlayerState
) {
    item {
        val thumbnailHeight = ThumbnailSelector.calculateThumbnailHeight(video?.videos)
        val thumbnail = ThumbnailSelector.selectThumbnail(video?.videos).url
        var isPlaying by rememberSaveable { mutableStateOf(false) }
        var isFullScreen by rememberSaveable { mutableStateOf(false) }

        val systemUiController = rememberSystemUiController()

        LaunchedEffect(isFullScreen) {
            systemUiController.isSystemBarsVisible = !isFullScreen
        }

        Box(
            modifier = if (isFullScreen) {
                Modifier.fillMaxSize()
            } else {
                Modifier
            }
        ) {
            if (isPlaying) {
                video?.videos?.large?.url?.let { videoUrl ->
                    VideoPlayer(
                        url = videoUrl,
                        videoPlayerState = videoPlayerState,
                        modifier = Modifier
                            .height(thumbnailHeight)
                            .fillMaxWidth()
                            .clip(shape = MaterialTheme.shapes.medium),
                        onFullScreenToggle = { fullScreen ->
                            isFullScreen = fullScreen
                        },
                        getOrCreatePlayer = getOrCreatePlayer,
                        updateVideoPlayerState = updateVideoPlayerState
                    )
                }
            } else {
                DynamicAsyncImage(
                    contentDescription = "Video Poster",
                    modifier = Modifier
                        .height(thumbnailHeight)
                        .fillMaxWidth()
                        .clip(shape = MaterialTheme.shapes.medium),
                    imageUrl = thumbnail
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(100.dp)
                        .height(64.dp)
                        .background(
                            Color.Black.copy(alpha = 0.7f), MaterialTheme.shapes.extraLarge
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
        onFullScreenToggle: (Boolean) -> Unit,
        getOrCreatePlayer: (String) -> ExoPlayer,
        videoPlayerState: VideoPlayerState,
        updateVideoPlayerState: (VideoPlayerState) -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val playerState = rememberSaveable(stateSaver = VideoPlayerStateSaver) {
        mutableStateOf(mutableStateOf(videoPlayerState))
    }

    val exoPlayer = remember(url) {
        getOrCreatePlayer(url)
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

    val fullScreenButton = remember {
        playerView.findViewById<ImageView>(androidx.media3.ui.R.id.exo_fullscreen)?.apply {
            setOnClickListener {
                val newFullScreenState = !playerState.value.value.isFullScreen
                playerState.value.value =
                    playerState.value.value.copy(isFullScreen = newFullScreenState)
                onFullScreenToggle(newFullScreenState)
            }
        }
    }

    LaunchedEffect(playerState.value.value.isFullScreen) {
        fullScreenButton?.setImageResource(
            if (playerState.value.value.isFullScreen) {
                R.drawable.ic_fullscreen_exit
            } else {
                R.drawable.ic_fullscreen
            }
        )
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

