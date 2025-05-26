package alireza.nezami.detail.presentation

import alireza.nezami.common.utils.base.BaseViewModel
import alireza.nezami.common.utils.extensions.orZero
import alireza.nezami.common.utils.result.Result
import alireza.nezami.common.utils.result.asResult
import alireza.nezami.detail.navigation.DetailArgs
import alireza.nezami.detail.presentation.contract.DetailEvent
import alireza.nezami.detail.presentation.contract.DetailIntent
import alireza.nezami.detail.presentation.contract.DetailUiState
import alireza.nezami.detail.presentation.contract.VideoPlayerManager
import alireza.nezami.detail.presentation.contract.VideoPlayerState
import alireza.nezami.domain.usecase.AddBookmarkUseCase
import alireza.nezami.domain.usecase.GetBookmarkByIdUseCase
import alireza.nezami.domain.usecase.GetVideoByIdUseCase
import alireza.nezami.domain.usecase.RemoveBookmarkUseCase
import alireza.nezami.model.domain.VideoHitDM
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val addBookmarkUseCase: AddBookmarkUseCase,
        private val removeBookmarkUseCase: RemoveBookmarkUseCase,
        private val getVideoByIdUseCase: GetVideoByIdUseCase,
        private val getBookmarkByIdUseCase: GetBookmarkByIdUseCase,
        private val videoPlayerManager: VideoPlayerManager
) : BaseViewModel<DetailUiState, DetailUiState.PartialState, DetailEvent, DetailIntent>(
    savedStateHandle, DetailUiState()
) {
    init {
        Timber.d("DetailViewModel initialized")
    }

    private var _videoPlayerState = mutableStateOf(VideoPlayerState())
    val videoPlayerState: State<VideoPlayerState> = _videoPlayerState

    fun updateVideoPlayerState(state: VideoPlayerState) {
        _videoPlayerState.value = state
    }

    fun getOrCreatePlayer(url: String): ExoPlayer {
        Timber.d("getOrCreatePlayer: $url")
        return videoPlayerManager.getPlayer(url).also { player ->
            if (player.playbackState == Player.STATE_IDLE || player.playbackState == Player.STATE_ENDED) {
                player.playWhenReady = _videoPlayerState.value.isPlaying
                player.seekTo(_videoPlayerState.value.playbackPosition)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        videoPlayerManager.releasePlayer()
    }


    private val video: VideoHitDM? = DetailArgs.fromSavedStateHandle(savedStateHandle)?.video
    private val videoId: Int = video?.id.orZero()

    init {
        acceptIntent(DetailIntent.GetVideoDetail(video))
        acceptIntent(DetailIntent.GetIsVideoBookmarked(videoId))
    }

    override fun mapIntents(intent: DetailIntent): Flow<DetailUiState.PartialState> =
        when (intent) {
            is DetailIntent.GetVideoDetail -> getVideoDetail(intent.video)
            is DetailIntent.OnBookmarkClick -> markVideoAsBookmarked(uiState.value.video)
            DetailIntent.OnNavigateBackClick -> {
                publishEvent(DetailEvent.NavigateBack)
                emptyFlow()
            }

            is DetailIntent.GetIsVideoBookmarked -> getIsVideoBookmarked(intent.id)
        }

    override fun reduceUiState(
            previousState: DetailUiState, partialState: DetailUiState.PartialState
    ): DetailUiState = when (partialState) {
        is DetailUiState.PartialState.AddVideoDetail -> previousState.copy(
            video = partialState.detail, isLoading = false, isError = false
        )

        is DetailUiState.PartialState.Error -> previousState.copy(
            isError = true, isLoading = false
        )

        is DetailUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

        is DetailUiState.PartialState.IsBookmarked -> previousState.copy(
            isBookmarked = partialState.isBookmarked
        )

        is DetailUiState.PartialState.UpdateBookmark -> previousState.copy(
            video = previousState.video?.copy(isBookmarked = !previousState.video.isBookmarked),
            isBookmarked = !previousState.isBookmarked
        )
    }


    private fun getVideoDetail(video: VideoHitDM?): Flow<DetailUiState.PartialState> = flow {
        if (video != null){
            emit(DetailUiState.PartialState.AddVideoDetail(video))
        } else {
            getVideoByIdUseCase(id = videoId).asResult().map {
                when (it) {
                    is Result.Error -> emit(DetailUiState.PartialState.Error(it.exception?.message.orEmpty()))
                    Result.Loading -> emit(DetailUiState.PartialState.Loading(true))
                    is Result.Success -> emit(DetailUiState.PartialState.AddVideoDetail(it.data.firstOrNull()))
                }
            }.catch {
                emit(DetailUiState.PartialState.Error(it.message.orEmpty()))
            }.collect()
        }
    }

    private fun getIsVideoBookmarked(id: Int): Flow<DetailUiState.PartialState> = flow {
        getBookmarkByIdUseCase(id = id).asResult().map {
            when (it) {
                is Result.Success -> emit(DetailUiState.PartialState.IsBookmarked(true))
                else -> emit(DetailUiState.PartialState.IsBookmarked(false))
            }
        }.catch {
            emit(DetailUiState.PartialState.Error(it.message.orEmpty()))
        }.collect()
    }

    private fun markVideoAsBookmarked(
            video: VideoHitDM?
    ): Flow<DetailUiState.PartialState> = flow {
        video?.let {
            if (video.isBookmarked) {
                removeBookmarkUseCase(video.id).asResult().map {
                    when (it) {
                        is Result.Success -> emit(DetailUiState.PartialState.UpdateBookmark(video))
                        else -> {}
                    }
                }.catch {
                    emit(DetailUiState.PartialState.Error(it.message.orEmpty()))
                }.collect()
            } else {
                addBookmarkUseCase(video).asResult().map {
                    when (it) {
                        is Result.Success -> emit(DetailUiState.PartialState.UpdateBookmark(video))
                        else -> {}
                    }
                }.catch {
                    emit(DetailUiState.PartialState.Error(it.message.orEmpty()))
                }.collect()
            }
        }
    }


}