package alireza.nezami.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoVariantsDM(
    val large: VideoFileDM?,
    val medium: VideoFileDM?,
    val small: VideoFileDM?,
    val tiny: VideoFileDM?
) : Parcelable
