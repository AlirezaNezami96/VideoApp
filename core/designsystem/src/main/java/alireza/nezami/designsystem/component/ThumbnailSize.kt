package alireza.nezami.designsystem.component

import alireza.nezami.model.domain.VideoVariantsDM
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ThumbnailSize(
        val url: String, val width: Int, val height: Double
)

object ThumbnailSelector {
    private const val LARGE_HEIGHT_THRESHOLD = 1080
    private const val MEDIUM_HEIGHT_THRESHOLD = 720
    private const val SMALL_HEIGHT_THRESHOLD = 360

    @Composable
    fun selectThumbnail(variants: VideoVariantsDM?): ThumbnailSize {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val density = LocalDensity.current

        // Convert dp to pixels
        val screenHeightPx = with(density) { screenHeight.dp.toPx() }

        return when {
            screenHeightPx >= LARGE_HEIGHT_THRESHOLD && variants?.large != null -> ThumbnailSize(
                variants.large?.thumbnail.orEmpty(),
                variants.large?.width ?: 0,
                variants.large?.height ?: 0.0
            )

            screenHeightPx >= MEDIUM_HEIGHT_THRESHOLD && variants?.medium != null -> ThumbnailSize(
                variants.large?.thumbnail.orEmpty(),
                variants.large?.width ?: 0,
                variants.large?.height ?: 0.0
            )

            screenHeightPx >= SMALL_HEIGHT_THRESHOLD && variants?.small != null -> ThumbnailSize(
                variants.large?.thumbnail.orEmpty(),
                variants.large?.width ?: 0,
                variants.large?.height ?: 0.0
            )

            variants?.tiny != null -> ThumbnailSize(
                variants.large?.thumbnail.orEmpty(),
                variants.large?.width ?: 0,
                variants.large?.height ?: 0.0
            )

            else -> variants?.let {
                it.small?.let { small ->
                    ThumbnailSize(
                        small.thumbnail, small.width, small.height
                    )
                } ?: it.medium?.let { medium ->
                    ThumbnailSize(
                        medium.thumbnail, medium.width, medium.height
                    )
                } ?: it.large?.let { large ->
                    ThumbnailSize(
                        large.thumbnail, large.width, large.height
                    )
                }
            } ?: ThumbnailSize("", 0, 0.0)
        }
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