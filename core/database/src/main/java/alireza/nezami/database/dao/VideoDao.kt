package alireza.nezami.database.dao

import alireza.nezami.model.entity.VideoEntity
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [VideoEntity] access
 */
@Dao
interface VideoDao {
    @Transaction
    @Query(value = "SELECT * FROM videos")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Transaction
    @Query(value = "SELECT * FROM videos WHERE id = :id LIMIT 1")
    fun getVideoById(id: Int): Flow<VideoEntity?>

    @Query("DELETE FROM videos")
    suspend fun deleteAllVideos()

    @Query("UPDATE videos SET isBookmarked = 1 WHERE id IN (:ids)")
    suspend fun updateBookmarkedStatus(ids: List<Int>)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: Int)
}
