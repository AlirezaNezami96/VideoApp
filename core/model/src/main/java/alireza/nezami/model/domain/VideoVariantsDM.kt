package alireza.nezami.model.domain

import alireza.nezami.model.entity.VideoVariantsEntity
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoVariantsDM(
        val large: VideoFileDM?,
        val medium: VideoFileDM?,
        val small: VideoFileDM?,
        val tiny: VideoFileDM?
) : Parcelable {
    fun toEntity() = VideoVariantsEntity(
        large = large?.toEntity(),
        medium = medium?.toEntity(),
        small = small?.toEntity(),
        tiny = tiny?.toEntity()
    )
}
