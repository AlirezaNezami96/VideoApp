package alireza.nezami.detail.presentation.contract

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VideoPlayerManager @Inject constructor(@ApplicationContext private val context: Context) {
    private var exoPlayer: ExoPlayer? = null
    private var currentUrl: String? = null
    private var isPlayerPrepared = false

    @OptIn(UnstableApi::class)
    fun getPlayer(url: String): ExoPlayer {
        if (exoPlayer == null) {
            createNewPlayer(url)
        } else if (currentUrl != url) {
            createNewPlayer(url)
        } else if (!isPlayerPrepared) {
            preparePlayer(url)
        }

        return exoPlayer!!
    }

    @OptIn(UnstableApi::class)
    private fun createNewPlayer(url: String) {
        releasePlayer()
        exoPlayer = ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build()
        preparePlayer(url)
    }

    private fun preparePlayer(url: String) {
        exoPlayer?.apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            currentUrl = url
            isPlayerPrepared = true
        }
    }

    fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
        currentUrl = null
        isPlayerPrepared = false
    }

    fun getPlayerOrNull() = exoPlayer
}