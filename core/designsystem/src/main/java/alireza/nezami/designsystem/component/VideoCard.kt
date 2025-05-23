package alireza.nezami.designsystem.component

import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.theme.VideoAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VideoCard(
        modifier: Modifier = Modifier,
        thumbnail: String,
        userName: String,
        userAvatar: String,
        isBookmarked: Boolean,
        height: Double,
        tagsList: List<String>,
        onVideoCardClick: () -> Unit
) {


    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
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
                    .height(height.dp)
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
                    tint = MaterialTheme.colorScheme.surface,
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
                        Color.Black.copy(alpha = 0.4f),
                        RoundedCornerShape(bottomEnd = 8.dp, topStart = 16.dp)
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
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(24.dp)
                )

            }

        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsList(tagsList: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tagsList.forEach { tag ->
            Text(
                text = tag,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.background
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
        DynamicAsyncImage(imageUrl = userAvatar,
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
            text = userName, style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.surface, fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Preview
@Composable
fun VideoCardPreview() {
    VideoAppTheme {
        VideoCard(thumbnail = "https://cdn.pixabay.com/video/2015/08/08/125-135736646_large.jpg",
            userName = "John Doe",
            userAvatar = "https://cdn.pixabay.com/user/2015/10/16/09-28-45-303_250x250.png",
            height = 200.0,
            tagsList = listOf("Action", "Adventure", "Drama"),
            isBookmarked = false,
            onVideoCardClick = {})
    }
}
