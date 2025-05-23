package alireza.nezami.model.domain

import alireza.nezami.model.entity.VideoFileEntity
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoFileDM(
        val url: String, val width: Int, val height: Int, val size: Int, val thumbnail: String
) : Parcelable

fun VideoFileDM.toEntity() = VideoFileEntity(
    url = url, width = width, height = height, size = size, thumbnail = thumbnail
)
