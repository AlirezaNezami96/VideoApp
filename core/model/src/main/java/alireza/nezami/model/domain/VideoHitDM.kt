package alireza.nezami.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class VideoHitDM(
        val id: Int,
        val pageURL: String,
        val type: String,
        val tags: String,
        val duration: Int,
        val videos: VideoVariantsDM?,
        val views: Int,
        val downloads: Int,
        val likes: Int,
        val comments: Int,
        val userId: Int,
        val user: String,
        val userImageURL: String,
        val isBookmarked: Boolean = false
) : Parcelable
