package alireza.nezami.designsystem.component

import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.theme.LocalTintTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import java.io.File

/**
 * A wrapper around [AsyncImage] which determines the colorFilter based on the theme
 */
@Composable
fun DynamicAsyncImage(
        imageUrl: String,
        contentDescription: String?,
        modifier: Modifier = Modifier,
) {
    val imageLoader = LocalContext.current.let { context ->
        ImageLoader.Builder(context)
            .diskCache {
                DiskCache.Builder()
                    .directory(File(context.cacheDir, "image_cache"))
                    .maxSizeBytes(250L * 1024 * 1024)
                    .build()
            }
            .build()
    }
    val iconTint = LocalTintTheme.current.iconTint
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        colorFilter = if (iconTint != null) ColorFilter.tint(iconTint) else null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        imageLoader = imageLoader,
        placeholder = painterResource(R.drawable.ic_video),
        error = painterResource(R.drawable.ic_video)

    )
}
