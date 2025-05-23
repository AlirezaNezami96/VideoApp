package alireza.nezami.model.entity

import alireza.nezami.model.domain.VideoFileDM
import kotlinx.serialization.Serializable

@Serializable
data class VideoFileEntity(
    val url: String,
    val width: Int,
    val height: Double,
    val size: Int,
    val thumbnail: String
) {
    companion object{
        val EMPTY = VideoFileEntity(
            url = "",
            width = 0,
            height = 0.0,
            size = 0,
            thumbnail = ""
        )
    }
}

fun VideoFileEntity.toDM() = VideoFileDM(
    url = url,
    width = width,
    height = height,
    size = size,
    thumbnail = thumbnail
)