package alireza.nezami.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoFileDM(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val thumbnail: String
) : Parcelable
