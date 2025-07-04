package alireza.nezami.videoapp.main

import alireza.nezami.domain.usecase.GetPopularVideosUseCase
import alireza.nezami.model.domain.VideoHitDM
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
        private val getPopularVideosUseCase: GetPopularVideosUseCase,
) : ViewModel() {
    init {
//        getPopularList()
    }

    val uiState: MutableStateFlow<MainActivityUiState> =
        MutableStateFlow(MainActivityUiState.Success(emptyList()))

    private fun getPopularList() {
        viewModelScope.launch {
            getPopularVideosUseCase.invoke().map {
                uiState.value = MainActivityUiState.Success(it)
            }.stateIn(
                scope = viewModelScope,
                initialValue = MainActivityUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000),
            ).collect()
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: List<VideoHitDM>) : MainActivityUiState
}
