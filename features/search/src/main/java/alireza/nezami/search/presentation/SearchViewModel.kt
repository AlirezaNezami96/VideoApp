package alireza.nezami.search.presentation

import alireza.nezami.common.utils.base.BaseViewModel
import alireza.nezami.common.utils.flattenMerge
import alireza.nezami.common.utils.result.Result
import alireza.nezami.common.utils.result.asResult
import alireza.nezami.domain.usecase.AddBookmarkUseCase
import alireza.nezami.domain.usecase.RemoveBookmarkUseCase
import alireza.nezami.domain.usecase.SearchVideosUseCase
import alireza.nezami.model.domain.ListState
import alireza.nezami.model.domain.VideoHitDM
import alireza.nezami.search.presentation.contract.SearchEvent
import alireza.nezami.search.presentation.contract.SearchIntent
import alireza.nezami.search.presentation.contract.SearchUiState
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val searchVideosUseCase: SearchVideosUseCase,
        private val addBookmarkUseCase: AddBookmarkUseCase,
        private val removeBookmarkUseCase: RemoveBookmarkUseCase
) : BaseViewModel<SearchUiState, SearchUiState.PartialState, SearchEvent, SearchIntent>(
    savedStateHandle, SearchUiState()
) {
    private var searchJob: Job? = null

    override fun mapIntents(intent: SearchIntent): Flow<SearchUiState.PartialState> =
        when (intent) {
            is SearchIntent.OnVideoClick -> {
                publishEvent(SearchEvent.NavigateToVideoDetail(intent.video))
                emptyFlow()
            }

            is SearchIntent.EnterSearchQuery -> flattenMerge(
                flowOf(SearchUiState.PartialState.EnterSearchQuery(intent.query)), searchVideos(
                    page = 1, query = MutableStateFlow(intent.query)
                )
            )

            SearchIntent.Paginate -> {
                with(uiState.value) {
                    if (searchResultList.isNotEmpty()) {
                        searchVideos(
                            page = searchResultState.nextPage, query = MutableStateFlow(searchQuery)
                        )
                    } else emptyFlow()
                }
            }

            is SearchIntent.OnBookmarkClick -> markVideoAsBookmarked(
                intent.video
            )
        }

    override fun reduceUiState(
            previousState: SearchUiState, partialState: SearchUiState.PartialState
    ): SearchUiState = when (partialState) {
        is SearchUiState.PartialState.AddSearchResult -> previousState.copy(
            searchResultState = previousState.searchResultState.copy(
                page = previousState.searchResultState.nextPage,
                state = if (partialState.videos.isEmpty()) ListState.EMPTY
                else ListState.IDLE
            ), searchResultList = if (previousState.searchResultState.page == 1) partialState.videos
            else addVideosToList(previousState.searchResultList, partialState.videos)
        )

        is SearchUiState.PartialState.SearchResultLoading -> previousState.copy(
            searchResultState = previousState.searchResultState.copy(
                state = if (previousState.searchResultList.isEmpty()) ListState.LOADING
                else ListState.PAGINATING
            )
        )

        is SearchUiState.PartialState.SearchResultError -> previousState.copy(
            searchResultState = previousState.searchResultState.copy(
                state = ListState.ERROR, errorMessage = partialState.message
            )
        )

        is SearchUiState.PartialState.EnterSearchQuery -> previousState.copy(
            searchQuery = partialState.query,
            searchResultState = previousState.searchResultState.copy(
                page = 1
            ),
            searchResultList = if (partialState.query.isEmpty()) emptyList()
            else previousState.searchResultList
        )

        is SearchUiState.PartialState.UpdateBookmark -> previousState.copy(
            searchResultList = previousState.searchResultList.map {
                if (it.id == partialState.video.id) it.copy(isBookmarked = !it.isBookmarked)
                else it
            })

    }

    private fun addVideosToList(
            previousList: List<VideoHitDM>, newList: List<VideoHitDM>
    ): List<VideoHitDM> {
        val existingIds = previousList.map { it.id }.toSet()
        val uniqueNewVideos = newList.filterNot { it.id in existingIds }

        return previousList + uniqueNewVideos
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun searchVideos(
            page: Int,
            query: StateFlow<String>
    ): Flow<SearchUiState.PartialState> = channelFlow {
        searchJob?.cancel()
        searchJob = launch {
            delay(500)
            query.filter { newQuery -> newQuery.isNotBlank() }
                .flatMapLatest { newQuery ->
                    flow {
                        var currentPage = page
                        var accumulatedResults = emptyList<VideoHitDM>()
                        var shouldFetchMore = true

                        while (shouldFetchMore) {
                            searchVideosUseCase(
                                page = currentPage,
                                query = newQuery,
                                filterByDuration = true
                            ).asResult().collect { result ->
                                when (result) {
                                    is Result.Error -> {
                                        shouldFetchMore = false
                                        send(SearchUiState.PartialState.SearchResultError(
                                            result.exception?.message.orEmpty()
                                        ))
                                    }
                                    Result.Loading -> {
                                        send(SearchUiState.PartialState.SearchResultLoading(true))
                                    }
                                    is Result.Success -> {
                                        val newResults = result.data
                                        accumulatedResults = if (currentPage == page) {
                                            newResults
                                        } else {
                                            addVideosToList(accumulatedResults, newResults)
                                        }

                                        // Send results immediately if we have any
                                        if (accumulatedResults.isNotEmpty()) {
                                            send(SearchUiState.PartialState.AddSearchResult(accumulatedResults))
                                        }

                                        // Continue fetching if needed
                                        if (accumulatedResults.size < 10 && newResults.isNotEmpty()) {
                                            currentPage++
                                        } else {
                                            shouldFetchMore = false
                                        }
                                    }
                                }
                            }
                        }
                        emit(Unit)
                    }
                }.collect()
        }
    }
    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

    private fun markVideoAsBookmarked(
            video: VideoHitDM
    ): Flow<SearchUiState.PartialState> = flow {
        if (video.isBookmarked) {
            removeBookmarkUseCase(video.id).asResult().map {
                when (it) {
                    is Result.Success -> emit(SearchUiState.PartialState.UpdateBookmark(video))
                    else -> {}
                }
            }.catch {
                emit(SearchUiState.PartialState.SearchResultError(it.message.orEmpty()))
            }.collect()
        } else {
            addBookmarkUseCase(video).asResult().map {
                when (it) {
                    is Result.Success -> emit(SearchUiState.PartialState.UpdateBookmark(video))
                    else -> {}
                }
            }.catch {
                emit(SearchUiState.PartialState.SearchResultError(it.message.orEmpty()))
            }.collect()
        }
    }

}