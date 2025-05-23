package alireza.nezami.data.util

import alireza.nezami.model.entity.VideoEntity
import alireza.nezami.model.entity.VideoFileEntity
import alireza.nezami.model.entity.VideoVariantsEntity
import alireza.nezami.model.data.VideoFileDto
import alireza.nezami.model.data.VideoHitDto
import javax.inject.Inject

class VideoMapper @Inject constructor() {
    fun mapToEntities(videoResponses: List<VideoHitDto>): List<VideoEntity> {
        return videoResponses.map { response ->
            VideoEntity(
                id = response.id ?: 0,
                pageURL = response.pageURL.orEmpty(),
                type = response.type.orEmpty(),
                tags = response.tags.orEmpty(),
                duration = response.duration ?: 0,
                videos = response.videos?.let { variants ->
                    VideoVariantsEntity(
                        large = variants.large?.toEntity(),
                        medium = variants.medium?.toEntity(),
                        small = variants.small?.toEntity(),
                        tiny = variants.tiny?.toEntity()
                    )
                },
                views = response.views ?: 0,
                downloads = response.downloads ?: 0,
                likes = response.likes ?: 0,
                comments = response.comments ?: 0,
                userId = response.user_id ?: 0,
                user = response.user.orEmpty(),
                userImageURL = response.userImageURL.orEmpty(),
                isBookmarked = false
            )
        }
    }

    private fun VideoFileDto.toEntity(): VideoFileEntity {
        return VideoFileEntity(
            url = url.orEmpty(),
            width = width ?: 0,
            height = height ?: 0.0,
            size = size ?: 0,
            thumbnail = thumbnail.orEmpty()
        )
    }
}