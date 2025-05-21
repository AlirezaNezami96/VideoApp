package alireza.nezami.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoResponseDM(
        val total: Int, val totalHits: Int, val hits: List<VideoHitDM>
) : Parcelable
