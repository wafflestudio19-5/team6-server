package waffle.team6.carrot.image.dto

import org.springframework.core.io.InputStreamResource
import waffle.team6.carrot.image.model.Image
import java.time.LocalDateTime

class ImageDto {
    data class UploadResponse(
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

    data class DownloadResponse(
        val image: InputStreamResource
    )
}