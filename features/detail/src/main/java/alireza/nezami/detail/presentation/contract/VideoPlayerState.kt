package alireza.nezami.detail.presentation.contract

data class VideoPlayerState(
        val isFullScreen: Boolean = false,
        val isPlaying: Boolean = false,
        val playbackPosition: Long = 0L,
)
