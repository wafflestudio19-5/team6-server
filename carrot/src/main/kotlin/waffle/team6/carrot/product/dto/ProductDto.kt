package waffle.team6.carrot.product.dto

import jdk.jfr.BooleanFlag
import org.hibernate.validator.constraints.Length
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.model.Status
import waffle.team6.carrot.user.dto.UserDto
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

class ProductDto {
    data class Response(
        val id: Long,
        val user: UserDto.Response,
        val images: List<Long>,
        val title: String,
        val content: String,
        val price: Long,
        val negotiable: Boolean,
        val category: String,
        val location: String,
        val hit: Long,
        val like: Long,
        val chat: Long,
        val priceSuggestion: Long,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        constructor(product: Product): this(
            id = product.id,
            user = UserDto.Response(product.user),
            images = product.images,
            title = product.title,
            content = product.content,
            price = product.price,
            negotiable = product.negotiable,
            category = product.category,
            location = product.location,
            hit = product.hit,
            like = product.likes,
            chat = product.chat,
            priceSuggestion = product.priceSuggestion,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }

    data class SimpleResponse(
        val id: Long,
        val user: UserDto.Response,
        val image: Long,
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
            user = UserDto.Response(product.user),
            image = product.images[0],
            title = product.title,
            price = product.price,
            location = product.location,
            like = product.likes,
            chat = product.chat,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }

    data class PostRequest(
        val images: List<Long>,
        @field:NotBlank
        val title: String,
        @field:Length(min = 1, max = 300)
        val content: String,
        @field:PositiveOrZero
        val price: Long,
        @field:BooleanFlag
        val negotiable: Boolean?,
        @field:NotBlank
        val category: String,
        @field:NotBlank
        val location: String
    )

    data class ModifyRequest(
        val images: List<Long>,
        @field:NotBlank
        val title: String,
        @field:Length(min = 1, max = 300)
        val content: String,
        @field:PositiveOrZero
        val price: Long,
        @field:BooleanFlag
        val negotiable: Boolean,
        @field:NotBlank
        val category: String,
    )

    data class PatchRequest(
        val images: List<Long>? = null,
        val title: String? = null,
        val content: String? = null,
        val price: Long? = null,
        val negotiable: Boolean? = null,
        val category: String? = null,
        val status: Status? = null,
    )
}