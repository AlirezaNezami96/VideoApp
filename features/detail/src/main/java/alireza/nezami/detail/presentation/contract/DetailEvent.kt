package alireza.nezami.detail.presentation.contract


sealed class DetailEvent {
    data object NavigateBack : DetailEvent()
}
