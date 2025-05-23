package alireza.nezami.database.entity

import kotlinx.serialization.Serializable

@Serializable
data class VideoFileEntity(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val thumbnail: String
) {
    companion object{
        val EMPTY = VideoFileEntity(
            url = "",
            width = 0,
            height = 0,
            size = 0,
            thumbnail = ""
        )
    }
}