package alireza.nezami.home.presentation

import alireza.nezami.common.utils.base.BaseViewModel
import alireza.nezami.common.utils.result.Result
import alireza.nezami.common.utils.result.asResult
import alireza.nezami.domain.usecase.AddBookmarkUseCase
import alireza.nezami.domain.usecase.GetLatestVideosUseCase
import alireza.nezami.domain.usecase.GetPopularVideosUseCase
import alireza.nezami.domain.usecase.RemoveBookmarkUseCase
import alireza.nezami.home.presentation.contract.HomeEvent
import alireza.nezami.home.presentation.contract.HomeIntent
import alireza.nezami.home.presentation.contract.HomeTabState
import alireza.nezami.home.presentation.contract.HomesUiState
import alireza.nezami.model.domain.ListState
import alireza.nezami.model.domain.VideoHitDM
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val getLatestVideosUseCase: GetLatestVideosUseCase,
        private val getPopularVideosUseCase: GetPopularVideosUseCase,
        private val addBookmarkUseCase: AddBookmarkUseCase,
        private val removeBookmarkUseCase: RemoveBookmarkUseCase
) : BaseViewModel<HomesUiState, HomesUiState.PartialState, HomeEvent, HomeIntent>(
    savedStateHandle, HomesUiState()
) {

    init {
        acceptIntent(HomeIntent.GetPopularVideos(uiState.value.popularVideoState.page))
    }

    override fun mapIntents(intent: HomeIntent): Flow<HomesUiState.PartialState> = when (intent) {
        is HomeIntent.ChangeTab -> {
            decideTabAction(intent.selectedTabIndex)
            flowOf(HomesUiState.PartialState.ChangeTab(intent.selectedTabIndex))
        }


        is HomeIntent.GetPopularVideos -> getPopularVideos(
            intent.page ?: uiState.value.popularVideoState.nextPage
        )

        is HomeIntent.GetLatestVideos -> getLatestVideos(
            intent.page ?: uiState.value.latestVideoState.nextPage
        )

        is HomeIntent.OnVideoClick -> {
            publishEvent(HomeEvent.NavigateToVideoDetail(intent.video))
            emptyFlow()
        }

        HomeIntent.OnSearchClick -> {
            publishEvent(HomeEvent.NavigateToSearch)
            emptyFlow()
        }

        is HomeIntent.OnBookmarkClick -> markVideoAsBookmarked(
            intent.video
        )
    }


    override fun reduceUiState(
            previousState: HomesUiState, partialState: HomesUiState.PartialState
    ): HomesUiState = when (partialState) {
        is HomesUiState.PartialState.AddPopularVideos -> previousState.copy(
            popularVideoState = previousState.popularVideoState.copy(
                page = previousState.popularVideoState.nextPage, state = ListState.IDLE
            ), popularVideos = addVideosToList(
                previousState.popularVideos, partialState.videos
            )
        )

        is HomesUiState.PartialState.AddLatestVideos -> previousState.copy(
            latestVideoState = previousState.latestVideoState.copy(
                page = previousState.latestVideoState.nextPage, state = ListState.IDLE
            ), latestVideos = addVideosToList(
                previousState.latestVideos, partialState.videos
            )
        )

        is HomesUiState.PartialState.PopularLoading -> previousState.copy(
            popularVideoState = previousState.popularVideoState.copy(
                state = if (previousState.popularVideos.isEmpty()) ListState.LOADING
                else ListState.PAGINATING
            )
        )

        is HomesUiState.PartialState.PopularError -> previousState.copy(
            popularVideoState = previousState.popularVideoState.copy(
                state = ListState.ERROR, errorMessage = partialState.message
            )
        )

        is HomesUiState.PartialState.LatestLoading -> previousState.copy(
            latestVideoState = previousState.latestVideoState.copy(
                state = if (previousState.latestVideos.isEmpty()) ListState.LOADING
                else ListState.PAGINATING
            )
        )

        is HomesUiState.PartialState.LatestError -> previousState.copy(
            latestVideoState = previousState.latestVideoState.copy(
                state = ListState.ERROR, errorMessage = partialState.message
            )
        )

        is HomesUiState.PartialState.ChangeTab -> previousState.copy(
            selectedTabIndex = partialState.selectedTabIndex
        )

        is HomesUiState.PartialState.UpdateBookmark -> {
            if (previousState.selectedTabIndex == HomeTabState.Popular.index) {
                previousState.copy(
                    popularVideos = previousState.popularVideos.map {
                        if (it.id == partialState.video.id) it.copy(isBookmarked = !it.isBookmarked)
                        else it
                    })
            } else if (previousState.selectedTabIndex == HomeTabState.Latest.index) {
                previousState.copy(
                    latestVideos = previousState.latestVideos.map {
                        if (it.id == partialState.video.id) it.copy(isBookmarked = !it.isBookmarked)
                        else it
                    })
            } else {
                previousState
            }
        }
    }


    private fun decideTabAction(selectedTabIndex: Int) {
        with(uiState.value) {
            when {
                selectedTabIndex == HomeTabState.Popular.index && popularVideos.isEmpty() -> {
                    acceptIntent(HomeIntent.GetPopularVideos(uiState.value.popularVideoState.page))
                }

                selectedTabIndex == HomeTabState.Latest.index && latestVideos.isEmpty() -> {
                    acceptIntent(HomeIntent.GetLatestVideos(uiState.value.latestVideoState.page))
                }


                else -> {}
            }
        }
    }

    private fun addVideosToList(
            previousList: List<VideoHitDM>, newList: List<VideoHitDM>
    ): List<VideoHitDM> {
        val finalList = previousList.toMutableList().apply {
            addAll(newList)
        }
        return finalList
    }


    private fun getPopularVideos(page: Int): Flow<HomesUiState.PartialState> = flow {
        getPopularVideosUseCase(page).asResult().map {
            when (it) {
                is Result.Error -> emit(HomesUiState.PartialState.PopularError(it.exception?.message.orEmpty()))
                Result.Loading -> emit(HomesUiState.PartialState.PopularLoading(true))
                is Result.Success -> emit(HomesUiState.PartialState.AddPopularVideos(it.data))
            }
        }.catch {
            emit(HomesUiState.PartialState.PopularError(it.message.orEmpty()))
        }.collect()
    }

    private fun getLatestVideos(page: Int): Flow<HomesUiState.PartialState> = flow {
        getLatestVideosUseCase(page).asResult().map {
            when (it) {
                is Result.Error -> emit(HomesUiState.PartialState.LatestError(it.exception?.message.orEmpty()))
                Result.Loading -> emit(HomesUiState.PartialState.LatestLoading(true))
                is Result.Success -> emit(HomesUiState.PartialState.AddLatestVideos(it.data))
            }
        }.catch {
            emit(HomesUiState.PartialState.LatestError(it.message.orEmpty()))
        }.collect()
    }

    private fun markVideoAsBookmarked(
            video: VideoHitDM
    ): Flow<HomesUiState.PartialState> = flow {
        if (video.isBookmarked) {
            removeBookmarkUseCase(video.id).asResult().map {
                when (it) {
                    is Result.Success -> emit(HomesUiState.PartialState.UpdateBookmark(video))
                    else -> {}
                }
            }.catch {
                emit(HomesUiState.PartialState.LatestError(it.message.orEmpty()))
            }.collect()
        } else {
            addBookmarkUseCase(video).asResult().map {
                when (it) {
                    is Result.Success -> emit(HomesUiState.PartialState.UpdateBookmark(video))
                    else -> {}
                }
            }.catch {
                emit(HomesUiState.PartialState.LatestError(it.message.orEmpty()))
            }.collect()
        }
    }


}