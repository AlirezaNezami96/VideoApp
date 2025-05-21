package alireza.nezami.model.data

import alireza.nezami.model.domain.VideoVariantsDM
import kotlinx.serialization.Serializable

@Serializable
data class VideoVariantsDto(
        val large: VideoFileDto? = null,
        val medium: VideoFileDto? = null,
        val small: VideoFileDto? = null,
        val tiny: VideoFileDto? = null
) {
    fun toDM() = VideoVariantsDM(
        large = large?.toDM(), medium = medium?.toDM(), small = small?.toDM(), tiny = tiny?.toDM()
    )
}
