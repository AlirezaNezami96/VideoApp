package alireza.nezami.database.entity

import alireza.nezami.model.domain.VideoFileDM

data class VideoFileEntity(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val thumbnail: String
)

fun VideoFileEntity.asExternalModel(): VideoFileDM =
    VideoFileDM(
        url = url,
        width = width,
        height = height,
        size = size,
        thumbnail = thumbnail
    )

fun VideoFileDM.asEntity(): VideoFileEntity =
    VideoFileEntity(
        url = url,
        width = width,
        height = height,
        size = size,
        thumbnail = thumbnail
    )