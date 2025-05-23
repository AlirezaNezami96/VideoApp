package alireza.nezami.database.converter

import alireza.nezami.model.entity.VideoEntity
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object VideoConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromVideoEntityList(list: List<VideoEntity>?): String? {
        return list?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toVideoEntityList(value: String?): List<VideoEntity>? {
        return value?.let { json.decodeFromString(it) }
    }
}