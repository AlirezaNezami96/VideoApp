package alireza.nezami.model.entity

import alireza.nezami.model.domain.VideoVariantsDM
import kotlinx.serialization.Serializable

@Serializable
data class VideoVariantsEntity(
        val large: VideoFileEntity?,
        val medium: VideoFileEntity?,
        val small: VideoFileEntity?,
        val tiny: VideoFileEntity?
)

fun VideoVariantsEntity.toDM() = VideoVariantsDM(
    large = large?.toDM(),
    medium = medium?.toDM(),
    small = small?.toDM(),
    tiny = tiny?.toDM()
)
