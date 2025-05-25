package alireza.nezami.bookmark.presentation

import alireza.nezami.bookmark.presentation.contract.BookmarkEvent
import alireza.nezami.bookmark.presentation.contract.BookmarkIntent
import alireza.nezami.bookmark.presentation.contract.BookmarkUiState
import alireza.nezami.common.utils.base.BaseViewModel
import alireza.nezami.common.utils.result.Result
import alireza.nezami.common.utils.result.asResult
import alireza.nezami.domain.usecase.AddBookmarkUseCase
import alireza.nezami.domain.usecase.GetAllBookmarksUseCase
import alireza.nezami.domain.usecase.RemoveBookmarkUseCase
import alireza.nezami.model.domain.ListState
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.model.domain.VideoState
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val addBookmarkUseCase: AddBookmarkUseCase,
        private val removeBookmarkUseCase: RemoveBookmarkUseCase,
        private val getAllBookmarksUseCase: GetAllBookmarksUseCase
) : BaseViewModel<BookmarkUiState, BookmarkUiState.PartialState, BookmarkEvent, BookmarkIntent>(
    savedStateHandle, BookmarkUiState()
) {

    init {
        acceptIntent(BookmarkIntent.GetBookmarkVideos)
    }

    override fun mapIntents(intent: BookmarkIntent): Flow<BookmarkUiState.PartialState> =
        when (intent) {
            is BookmarkIntent.OnVideoClick -> {
                publishEvent(BookmarkEvent.NavigateToVideoDetail(intent.id))
                emptyFlow()
            }

            BookmarkIntent.GetBookmarkVideos -> getBookmarkVideos()

            is BookmarkIntent.OnBookmarkClick -> markVideoAsBookmarked(
                intent.video
            )
        }

    override fun reduceUiState(
            previousState: BookmarkUiState, partialState: BookmarkUiState.PartialState
    ): BookmarkUiState = when (partialState) {
        is BookmarkUiState.PartialState.AddBookmarkVideoList -> previousState.copy(
            bookmarkVideoList = partialState.videos, bookmarkListState = VideoState(
                state = if (partialState.videos.isNotEmpty()) ListState.IDLE
                else ListState.EMPTY
            )
        )

        is BookmarkUiState.PartialState.Error -> previousState.copy(
            bookmarkListState = previousState.bookmarkListState.copy(
                state = ListState.ERROR, errorMessage = partialState.errorMessage
            )
        )

        is BookmarkUiState.PartialState.Loading -> previousState.copy(
            bookmarkListState = previousState.bookmarkListState.copy(
                state = ListState.LOADING
            )
        )

        is BookmarkUiState.PartialState.UpdateBookmark -> previousState.copy(
            bookmarkVideoList = previousState.bookmarkVideoList.map {
                if (it.id == partialState.video.id) it.copy(isBookmarked = !it.isBookmarked)
                else it
            })
    }


    private fun getBookmarkVideos(): Flow<BookmarkUiState.PartialState> = flow {
        getAllBookmarksUseCase().asResult().map {
            when (it) {
                is Result.Error -> emit(BookmarkUiState.PartialState.Error(it.exception?.message.orEmpty()))
                Result.Loading -> emit(BookmarkUiState.PartialState.Loading(true))
                is Result.Success -> emit(BookmarkUiState.PartialState.AddBookmarkVideoList(it.data))
            }
        }.catch {
            emit(BookmarkUiState.PartialState.Error(it.message.orEmpty()))
        }.collect()
    }


    private fun markVideoAsBookmarked(
            video: VideoHitDM
    ): Flow<BookmarkUiState.PartialState> = flow {
        if (video.isBookmarked) {
            removeBookmarkUseCase(video.id).asResult().map {
                when (it) {
                    is Result.Success -> emit(BookmarkUiState.PartialState.UpdateBookmark(video))
                    else -> {}
                }
            }.catch {
                emit(BookmarkUiState.PartialState.Error(it.message.orEmpty()))
            }.collect()
        } else {
            addBookmarkUseCase(video).asResult().map {
                when (it) {
                    is Result.Success -> emit(BookmarkUiState.PartialState.UpdateBookmark(video))
                    else -> {}
                }
            }.catch {
                emit(BookmarkUiState.PartialState.Error(it.message.orEmpty()))
            }.collect()
        }
    }


}