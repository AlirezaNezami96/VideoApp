package alireza.nezami.data.util


sealed class SynchronizationState {
    data object Idle : SynchronizationState()
    data object InProgress : SynchronizationState()
    data class Success<T>(val data: List<T>) : SynchronizationState()
    data class Error(val message: String) : SynchronizationState()
}
