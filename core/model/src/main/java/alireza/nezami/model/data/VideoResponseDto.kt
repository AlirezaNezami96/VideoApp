package alireza.nezami.model.data

import alireza.nezami.model.domain.VideoResponseDM
import kotlinx.serialization.Serializable


@Serializable
data class VideoResponseDto(
        val total: Int? = null, val totalHits: Int? = null, val hits: List<VideoHitDto>? = null
) {
    fun toDM() = VideoResponseDM(total = total ?: 0,
        totalHits = totalHits ?: 0,
        hits = hits?.map { it.toDM() } ?: emptyList())
}
