package waffle.team6.carrot.product.dto

import java.time.LocalDateTime

class ProductDto {
    data class Response(
        val id: Long,
        // val picture:
        // val titile:
        // val contents:
        val price: Long,
        val negotiable: Boolean,
        val category: String,
        val hit: Long,
        val like: Long,
        val chat: Long,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    )
}