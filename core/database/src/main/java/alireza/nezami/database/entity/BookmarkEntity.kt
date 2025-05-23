package alireza.nezami.database.entity

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
