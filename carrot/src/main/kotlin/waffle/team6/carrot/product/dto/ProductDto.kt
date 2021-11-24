package waffle.team6.carrot.product.dto

import waffle.team6.carrot.product.model.Product
import java.time.LocalDateTime

class ProductDto {
    data class Response(
        val id: Long,
        // val pictures:
        val title: String,
        // val contents:
        val price: Long,
        val negotiable: Boolean,
        val category: String,
        val location: String,
        val hit: Long,
        val like: Long,
        val chat: Long,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    )

    data class SimpleResponse(
        val id: Long,
        // val picture,
        val title: String,
        val price: Long,
        val location: String,
        val like: Long,
        val chat: Long,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        constructor(product: Product): this(
            id = product.id,
            // picture = product.picture
            title = product.title,
            price = product.price,
            location = product.location,
            like = product.like,
            chat = product.chat,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }
}