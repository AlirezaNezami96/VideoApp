package alireza.nezami.model.domain

import alireza.nezami.model.entity.VideoFileEntity
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class VideoFileDM(
        val url: String, val width: Int, val height: Double, val size: Int, val thumbnail: String
) : Parcelable

fun VideoFileDM.toEntity() = VideoFileEntity(
    url = url, width = width, height = height, size = size, thumbnail = thumbnail
)
