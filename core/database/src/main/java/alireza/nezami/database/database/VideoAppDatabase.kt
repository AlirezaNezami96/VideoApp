package alireza.nezami.database.database

import alireza.nezami.database.converter.VideoConverters
import alireza.nezami.database.converter.VideoFileConverters
import alireza.nezami.database.converter.VideoVariantsConverters
import alireza.nezami.database.dao.BookmarkDao
import alireza.nezami.database.dao.VideoDao
import alireza.nezami.database.entity.BookmarkEntity
import alireza.nezami.database.entity.VideoEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [VideoEntity::class, BookmarkEntity::class], version = 1, exportSchema = false
)
@TypeConverters(
    VideoConverters::class, VideoFileConverters::class, VideoVariantsConverters::class
)
abstract class VideoAppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun bookmarkDao(): BookmarkDao
}
