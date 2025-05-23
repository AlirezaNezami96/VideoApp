package alireza.nezami.videoapp

import alireza.nezami.designsystem.component.VideoCard
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import alireza.nezami.videoapp.ui.theme.VideoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VideoCard(thumbnail = "https://cdn.pixabay.com/video/2015/08/08/125-135736646_large.jpg",
                        userName = "John Doe",
                        userAvatar = "https://cdn.pixabay.com/user/2015/10/16/09-28-45-303_250x250.png",
                        height = 300.0,
                        tagsList = listOf("Action", "Adventure", "Drama"),
                        isBookmarked = false,
                        onVideoCardClick = {})
                }
            }
        }
    }
}