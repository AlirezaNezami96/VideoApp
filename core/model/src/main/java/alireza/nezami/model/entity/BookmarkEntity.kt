package alireza.nezami.model.entity

import alireza.nezami.model.domain.VideoHitDM
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
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
        val userImageURL: String
)

fun BookmarkEntity.toDM() = VideoHitDM(
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
    isBookmarked = true
)
