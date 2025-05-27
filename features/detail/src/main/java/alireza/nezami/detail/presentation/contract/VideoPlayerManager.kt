package alireza.nezami.detail.presentation.contract

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoPlayerManager @Inject constructor(@ApplicationContext private val context: Context) {
    private var exoPlayer: ExoPlayer? = null
    private var currentUrl: String? = null
    private var lastPosition: Long = 0
    private var wasPlaying: Boolean = false

    fun getPlayer(url: String): ExoPlayer {
        return if (currentUrl == url && exoPlayer != null) {
            // Return existing player if URL matches
            exoPlayer!!
        } else {
            // Save state before releasing old player
            savePlayerState()
            releasePlayer()

            // Create new player
            ExoPlayer.Builder(context)
                .setHandleAudioBecomingNoisy(true)
                .build().apply {
                    setMediaItem(MediaItem.fromUri(url))
                    seekTo(lastPosition)
                    playWhenReady = wasPlaying
                    prepare()
                    exoPlayer = this
                    currentUrl = url
                }
        }
    }

    fun savePlayerState() {
        exoPlayer?.let {
            lastPosition = it.currentPosition
            wasPlaying = it.isPlaying
        }
    }

    fun restorePlayerState(): Pair<Long, Boolean> {
        return Pair(lastPosition, wasPlaying)
    }

    fun releasePlayer() {
        savePlayerState()
        exoPlayer?.release()
        exoPlayer = null
        currentUrl = null
    }

    fun pausePlayer() {
        savePlayerState()
        exoPlayer?.pause()
    }

    fun resumePlayer() {
        exoPlayer?.let { player ->
            if (wasPlaying) {
                player.play()
            }
        }
    }
}