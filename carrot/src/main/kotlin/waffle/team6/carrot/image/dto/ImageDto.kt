package waffle.team6.carrot.image.dto

import waffle.team6.carrot.image.model.Image
import java.time.LocalDateTime

class ImageDto {
    data class ImageResponse(
        val id: Long,
        val url: String,
        val updatedAt: LocalDateTime,
        val createdAt: LocalDateTime
    ) {
        constructor(image: Image): this(
            id = image.id,
            url = image.url,
            updatedAt = image.updatedAt,
            createdAt = image.createdAt
        )
    }

    data class ImageUrlResponse(
        val url: String
    )

    data class ImageListResponse(
        val count: Int,
        val contents: List<ImageResponse>
    ) {
        constructor(images: List<Image>): this(
            count = images.count(),
            contents = images.map { ImageResponse(it) }
        )
    }
}