package waffle.team6.carrot.product.dto

import jdk.jfr.BooleanFlag
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import waffle.team6.carrot.image.dto.ImageDto
import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.product.model.Category
import waffle.team6.carrot.product.model.ForAge
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.model.Status
import waffle.team6.carrot.user.dto.UserDto
import java.time.LocalDateTime
import javax.validation.constraints.*

class ProductDto {
    data class ProductResponse(
        val id: Long,
        val user: UserDto.Response,
        val images: List<Long>,
        val title: String,
        val content: String,
        val price: Long,
        val negotiable: Boolean,
        val category: Category,
        val forAge: ForAge?,
        val location: String,
        val hit: Long,
        val likes: Long,
        val chats: Long,
        val status: Status,
        val priceSuggestions: Long?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val lastBringUpMyPost: LocalDateTime
    ) {
        constructor(product: Product, isSeller: Boolean): this(
            id = product.id,
            user = UserDto.Response(product.user),
            images = product.images.map { it.id },
            title = product.title,
            content = product.content,
            price = product.price,
            negotiable = product.negotiable,
            category = product.category,
            forAge = product.forAge,
            location = product.location,
            hit = product.hit,
            likes = product.likes,
            chats = product.chats,
            status = product.status,
            priceSuggestions = if (isSeller) product.priceSuggestions else null,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt,
            lastBringUpMyPost = product.lastBringUpMyPost
        )
    }

    data class ProductSimpleResponse(
        val id: Long,
        val user: UserDto.Response,
        val image: Long,
        val title: String,
        val price: Long,
        val location: String,
        val likes: Long,
        val chats: Long,
        val status: Status,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val lastBringUpMyPost: LocalDateTime
    ) {
        constructor(product: Product): this(
            id = product.id,
            user = UserDto.Response(product.user),
            image = product.images[0].id,
            title = product.title,
            price = product.price,
            location = product.location,
            likes = product.likes,
            chats = product.chats,
            status = product.status,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt,
            lastBringUpMyPost = product.lastBringUpMyPost
        )
    }

    data class ProductPostRequest(
        val images: List<Long>,
        @field:NotBlank
        val title: String,
        @field:Length(min = 1, max = 300)
        val content: String,
        @field:PositiveOrZero
        val price: Long,
        @field:BooleanFlag
        val negotiable: Boolean?,
        @field:Range(min = 1, max = 17)
        val category: Int,
        @field:Range(min = 1, max = 6)
        val forAge: Int? = null,
        @field:NotBlank
        val location: String,
        @field:Range(min = 0, max = 3)
        val rangeOfLocation: Int
    )

    data class ProductUpdateRequest(
        val images: List<Long>? = null,
        @field:NotBlank
        val title: String? = null,
        @field:Length(min = 1, max = 300)
        val content: String? = null,
        @field:PositiveOrZero
        val price: Long? = null,
        @field:BooleanFlag
        val negotiable: Boolean? = null,
        @field:Range(min = 1, max = 17)
        val category: Int? = null,
        @field:Range(min = 1, max = 6)
        val forAge: Int?,
        @field:Range(min = 0, max = 3)
        val rangeOfLocation: Int
    )

    data class ProductSearchRequest(
        val pageNumber: Int,
        val pageSize: Int,
        val title: String,
        val rangeOfLocation: RangeOfLocation? = null,
        val categories: List<Category>? = null,
        val minPrice: Long? = null,
        val maxPrice: Long? = null
    )
}