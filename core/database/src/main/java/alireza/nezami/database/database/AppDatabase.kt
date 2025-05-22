package alireza.nezami.database.database

import alireza.nezami.database.converter.VideoConverters
import alireza.nezami.database.dao.BookmarkDao
import alireza.nezami.database.dao.VideoDao
import alireza.nezami.database.entity.VideoEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [VideoEntity::class], version = 1, exportSchema = false)
@TypeConverters(VideoConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun bookmarkDao(): BookmarkDao
}