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
    @Query(value = "SELECT * FROM videos WHERE `order` = 'POPULAR' AND duration > 60")
    fun getAllPopularVideos(): Flow<List<VideoEntity>>

    @Transaction
    @Query(value = "SELECT * FROM videos WHERE `order` = 'LATEST' AND duration > 60")
    fun getAllLatestVideos(): Flow<List<VideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Query("DELETE FROM videos WHERE type = :type")
    suspend fun deleteVideosByType(type: String)

}
