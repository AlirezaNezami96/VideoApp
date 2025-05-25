package alireza.nezami.database.dao

import alireza.nezami.model.entity.VideoEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [VideoEntity] access
 */
@Dao
interface VideoDao {
    @Transaction
    @Query(value = "SELECT * FROM videos WHERE type = 'POPULAR'")
    fun getAllPopularVideos(): Flow<List<VideoEntity>>

    @Transaction
    @Query(value = "SELECT * FROM videos WHERE type = 'LATEST'")
    fun getAllLatestVideos(): Flow<List<VideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Query("DELETE FROM videos WHERE type = :type")
    suspend fun deleteVideosByType(type: String)

    @Query("UPDATE videos SET isBookmarked = 1 WHERE id IN (:ids)")
    suspend fun updateBookmarkedStatus(ids: List<Int>)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: Int)
}
