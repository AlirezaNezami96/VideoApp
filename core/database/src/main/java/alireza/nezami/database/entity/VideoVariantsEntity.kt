package alireza.nezami.database.entity

import alireza.nezami.model.domain.VideoVariantsDM

data class VideoVariantsEntity(
        val large: VideoFileEntity?,
        val medium: VideoFileEntity?,
        val small: VideoFileEntity?,
        val tiny: VideoFileEntity?
)

fun VideoVariantsEntity.asExternalModel(): VideoVariantsDM = VideoVariantsDM(
    large = large?.asExternalModel(),
    medium = medium?.asExternalModel(),
    small = small?.asExternalModel(),
    tiny = tiny?.asExternalModel()
)

fun VideoVariantsDM.asEntity(): VideoVariantsEntity = VideoVariantsEntity(
    large = large?.asEntity(),
    medium = medium?.asEntity(),
    small = small?.asEntity(),
    tiny = tiny?.asEntity()
)