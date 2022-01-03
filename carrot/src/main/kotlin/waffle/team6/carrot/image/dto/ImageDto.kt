package waffle.team6.carrot.image.dto

import org.springframework.core.io.InputStreamResource
import waffle.team6.carrot.image.model.Image
import java.time.LocalDateTime

class ImageDto {
    data class ImageResponse(
        val id: Long,
        val updatedAt: LocalDateTime,
        val createdAt: LocalDateTime
    ) {
        constructor(image: Image): this(
            id = image.id,
            updatedAt = image.updatedAt,
            createdAt = image.createdAt
        )
    }

    data class ImageResource(
        val image: InputStreamResource
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