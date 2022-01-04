package waffle.team6.carrot.image.dto

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
        val contentType: String,
        val byteImage: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ImageResource

            if (!byteImage.contentEquals(other.byteImage)) return false

            return true
        }

        override fun hashCode(): Int {
            return byteImage.contentHashCode()
        }
    }

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