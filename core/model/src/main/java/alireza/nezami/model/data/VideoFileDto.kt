package alireza.nezami.model.data

import alireza.nezami.model.domain.VideoFileDM
import kotlinx.serialization.Serializable

@Serializable
data class VideoFileDto(
        val url: String? = null,
        val width: Int? = null,
        val height: Int? = null,
        val size: Int? = null,
        val thumbnail: String? = null
) {
    fun toDM() = VideoFileDM(
        url = url.orEmpty(),
        width = width ?: 0,
        height = height ?: 0,
        size = size ?: 0,
        thumbnail = thumbnail.orEmpty()
    )
}
