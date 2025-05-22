package alireza.nezami.database.entity

import alireza.nezami.database.converter.VideoConverters
import alireza.nezami.model.domain.VideoHitDM
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "videos")
@TypeConverters(VideoConverters::class)
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
        var isBookmarked: Boolean = false
)

fun VideoEntity.asExternalModel(): VideoHitDM = VideoHitDM(
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
    isBookmarked = isBookmarked
)

fun VideoHitDM.asEntity(): VideoEntity = VideoEntity(
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
    userImageURL = userImageURL,
    isBookmarked = isBookmarked
)