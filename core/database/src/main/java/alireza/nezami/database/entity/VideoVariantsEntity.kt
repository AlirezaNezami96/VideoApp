package alireza.nezami.database.entity

import kotlinx.serialization.Serializable

@Serializable
data class VideoVariantsEntity(
    val large: VideoFileEntity?,
    val medium: VideoFileEntity?,
    val small: VideoFileEntity?,
    val tiny: VideoFileEntity?
)