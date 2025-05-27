package alireza.nezami.model.entity

import alireza.nezami.model.data.VideoOrder
import alireza.nezami.model.domain.VideoHitDM
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "videos")
data class VideoEntity(
        @PrimaryKey val id: Int,
        val pageURL: String,
        val type: String,
        val tags: String,
        val duration: Int,
        val videos: VideoVariantsEntity?,
        val views: Int,
        val downloads: Int,
        val likes: Int,
        val comments: Int,
        val userId: Int,
        val user: String,
        val userImageURL: String,
        var isBookmarked: Boolean = false,
        val order: VideoOrder
)

fun VideoEntity.toDM() = VideoHitDM(
    id = id,
    pageURL = pageURL,
    type = type,
    tags = tags,
    duration = duration,
    videos = videos?.toDM(),
    views = views,
    downloads = downloads,
    likes = likes,
    comments = comments,
    userId = userId,
    user = user,
    userImageURL = userImageURL,
    isBookmarked = isBookmarked
)
