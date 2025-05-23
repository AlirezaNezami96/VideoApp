package alireza.nezami.database.entity

import kotlinx.serialization.Serializable

@Serializable
data class VideoFileEntity(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val thumbnail: String
)