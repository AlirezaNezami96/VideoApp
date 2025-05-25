package alireza.nezami.designsystem.component

import alireza.nezami.designsystem.R
import alireza.nezami.model.domain.VideoVariantsDM
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun VideoCard(
        modifier: Modifier = Modifier,
        videos: VideoVariantsDM?,
        userName: String,
        userAvatar: String,
        isBookmarked: Boolean,
        tagsList: List<String>,
        onVideoCardClick: () -> Unit,
        onBookmarkClick: () -> Unit
) {
    val thumbnailHeight = ThumbnailSelector.calculateThumbnailHeight(videos)
    val thumbnail = ThumbnailSelector.selectThumbnail(videos).url

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onVideoCardClick()
            }, shape = MaterialTheme.shapes.medium, colors = CardDefaults.cardColors()
    ) {
        Box(
            modifier = Modifier
        ) {

            DynamicAsyncImage(
                contentDescription = "Video Poster",
                modifier = Modifier
                    .height(thumbnailHeight)
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.medium),
                imageUrl = thumbnail
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(
                        Color.Black.copy(alpha = 0.4f), MaterialTheme.shapes.extraLarge
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play_video),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp)
                        .padding(8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        RoundedCornerShape(bottomEnd = 8.dp, topStart = 8.dp)
                    )
            ) {
                UserParts(
                    userName = userName,
                    userAvatar = userAvatar,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.5f),
                            )
                        )
                    )
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .weight(1f)
                ) {
                    TagsList(
                        tagsList = tagsList
                    )
                }

                val bookmarkIcon = if (isBookmarked) {
                    R.drawable.ic_bookmark_filled
                } else {
                    R.drawable.ic_bookmark_stroke
                }
                Icon(
                    painter = painterResource(bookmarkIcon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            onBookmarkClick()
                        }
                        .size(24.dp)
                )

            }

        }
    }
}

@Composable
fun TagsList(tagsList: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(tagsList) { tag ->
            Text(
                text = tag,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
fun UserParts(userName: String, userAvatar: String) {
    val goldGradient = Brush.sweepGradient(
        listOf(
            Color(0xFFFFD700), Color(0xFFFFA500), Color(0xFFFFE066), Color(0xFFFFD700)
        )
    )
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)
    ) {
        DynamicAsyncImage(
            imageUrl = userAvatar,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(24.dp)
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val radius = size.minDimension / 2
                    drawCircle(
                        brush = goldGradient,
                        radius = radius,
                        center = Offset(radius, radius),
                        style = Stroke(width = strokeWidth)
                    )
                }
                .padding(4.dp)
                .clip(RoundedCornerShape(50)))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = userName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold
            )
        )
    }
}