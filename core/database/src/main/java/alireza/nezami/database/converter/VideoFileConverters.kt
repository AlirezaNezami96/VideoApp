package alireza.nezami.database.converter

import alireza.nezami.model.entity.VideoFileEntity
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object VideoFileConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromVideoFile(value: VideoFileEntity?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toVideoFile(value: String?): VideoFileEntity? {
        return value?.let { json.decodeFromString(it) }
    }
}
