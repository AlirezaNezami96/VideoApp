package alireza.nezami.database.dao

import alireza.nezami.database.entity.VideoEntity
import androidx.room.Dao
import androidx.room.Query

/**
 * DAO for [VideoEntity] access
 */
@Dao
interface VideoDao {

    @Query("DELETE FROM videos")
    suspend fun deleteAllVideos()

    @Query("UPDATE videos SET isBookmarked = 1 WHERE id IN (:ids)")
    suspend fun updateBookmarkedStatus(ids: List<Int>)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: Int)
}
