package alireza.nezami.database.converter

import alireza.nezami.model.entity.VideoVariantsEntity
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object VideoVariantsConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromVideoVariants(value: VideoVariantsEntity?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toVideoVariants(value: String?): VideoVariantsEntity? {
        return value?.let { json.decodeFromString(it) }
    }
}