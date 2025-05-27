package alireza.nezami.designsystem.component

import alireza.nezami.model.domain.VideoVariantsDM
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import timber.log.Timber

data class ThumbnailSize(
        val url: String, val width: Int, val height: Double
)

object ThumbnailSelector {

    @Composable
    fun selectThumbnail(variants: VideoVariantsDM?): ThumbnailSize {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val density = LocalDensity.current
        val screenWidthPx = with(density) { screenWidth.dp.toPx() }

        // Calculate quality scores based on screen width and thumbnail dimensions
        fun calculateQualityScore(width: Int, size: String): Double {
            val sizeMultiplier = when (size) {
                "SMALL" -> 1.6  // Boost for small
                "MEDIUM" -> 1.1 // Preferred option
                "LARGE" -> 0.8  // Penalty for large
                else -> 1.0
            }

            return (1 - kotlin.math.abs(screenWidthPx - width) / screenWidthPx) * sizeMultiplier
        }

        val thumbnailSize = variants?.let { v ->
            // Calculate scores for available sizes
            val scores = mutableListOf<Pair<ThumbnailSize, Double>>()

            v.medium?.let {
                scores.add(
                    ThumbnailSize(it.thumbnail, it.width, it.height) to
                            calculateQualityScore(it.width, "MEDIUM")
                )
            }
            v.small?.let {
                scores.add(
                    ThumbnailSize(it.thumbnail, it.width, it.height) to
                            calculateQualityScore(it.width, "SMALL")
                )
            }
            v.large?.let {
                scores.add(
                    ThumbnailSize(it.thumbnail, it.width, it.height) to
                            calculateQualityScore(it.width, "LARGE")
                )
            }
            v.tiny?.let {
                scores.add(
                    ThumbnailSize(it.thumbnail, it.width, it.height) to
                            calculateQualityScore(it.width, "TINY")
                )
            }

            // Select the thumbnail with the highest score
            scores.maxByOrNull { it.second }?.first?.also { selected ->
                Timber.d("Selected optimized thumbnail: width=${selected.width}, height=${selected.height}, score=${scores.find { it.first == selected }?.second}")
            } ?: ThumbnailSize("", 0, 0.0).also {
                Timber.d("No suitable thumbnail found, using default empty thumbnail")
            }
        } ?: ThumbnailSize("", 0, 0.0).also {
            Timber.d("Variants null, using default empty thumbnail")
        }

        Timber.d("Screen width: ${screenWidthPx}px")
        return thumbnailSize
    }

    @Composable
    fun calculateThumbnailHeight(variants: VideoVariantsDM?): Dp {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val selectedThumbnail = selectThumbnail(variants)

        return if (selectedThumbnail.width > 0) {
            val aspectRatio = selectedThumbnail.height.toFloat() / selectedThumbnail.width.toFloat()
            (screenWidth * aspectRatio).dp
        } else {
            200.dp
        }
    }
}