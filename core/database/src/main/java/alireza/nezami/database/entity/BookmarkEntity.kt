package alireza.nezami.database.entity

import alireza.nezami.model.domain.VideoHitDM
import androidx.room.Entity
import androidx.room.PrimaryKey

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

fun BookmarkEntity.asExternalModel(): VideoHitDM =
    VideoHitDM(
        id = id,
        pageURL = pageURL,
        type = type,
        tags = tags,
        duration = duration,
        videos = videos?.asExternalModel(),
        views = views,
        downloads = downloads,
        likes = likes,
        comments = comments,
        userId = userId,
        user = user,
        userImageURL = userImageURL,
        isBookmarked = true
    )

fun VideoHitDM.asBookmarkEntity(): BookmarkEntity =
    BookmarkEntity(
        id = id,
        pageURL = pageURL,
        type = type,
        tags = tags,
        duration = duration,
        videos = videos?.asEntity(),
        views = views,
        downloads = downloads,
        likes = likes,
        comments = comments,
        userId = userId,
        user = user,
        userImageURL = userImageURL
    )