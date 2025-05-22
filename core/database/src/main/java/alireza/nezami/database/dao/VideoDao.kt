package alireza.nezami.database.dao

import alireza.nezami.database.entity.VideoEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos")
    suspend fun getAllVideos(): List<VideoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Query("DELETE FROM videos")
    suspend fun deleteAllVideos()

    @Query("UPDATE videos SET isBookmarked = 1 WHERE id IN (:ids)")
    suspend fun updateBookmarkedStatus(ids: List<Int>)
}