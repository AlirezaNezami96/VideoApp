package alireza.nezami.model.data

import kotlinx.serialization.Serializable

@Serializable
data class VideoHitDto(
    val id: Int? = null,
    val pageURL: String? = null,
    val type: String? = null,
    val tags: String? = null,
    val duration: Int? = null,
    val videos: VideoVariantsDto? = null,
    val views: Int? = null,
    val downloads: Int? = null,
    val likes: Int? = null,
    val comments: Int? = null,
    val user_id: Int? = null,
    val user: String? = null,
    val userImageURL: String? = null
) {
    fun toDM() = VideoHitDM(
        id = id ?: 0,
        pageURL = pageURL.orEmpty(),
        type = type.orEmpty(),
        tags = tags.orEmpty(),
        duration = duration ?: 0,
        videos = videos?.toDM(),
        views = views ?: 0,
        downloads = downloads ?: 0,
        likes = likes ?: 0,
        comments = comments ?: 0,
        userId = user_id ?: 0,
        user = user.orEmpty(),
        userImageURL = userImageURL.orEmpty()
    )
}
