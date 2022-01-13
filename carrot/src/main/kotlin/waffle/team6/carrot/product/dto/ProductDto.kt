package waffle.team6.carrot.product.dto

import jdk.jfr.BooleanFlag
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.product.model.Category
import waffle.team6.carrot.product.model.ForAge
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.model.ProductStatus
import waffle.team6.carrot.user.dto.UserDto
import java.time.LocalDateTime
import javax.validation.constraints.*

class ProductDto {
    data class ProductResponse(
        val id: Long,
        val user: UserDto.Response,
        val images: List<String>? = null,
        val title: String,
        val content: String,
        val price: Long,
        val negotiable: Boolean,
        val category: Category,
        val forAge: List<ForAge>? = null,
        val location: String,
        val rangeOfLocation: RangeOfLocation,
        val hit: Long,
        val likes: Long,
        val chats: Long,
        val status: ProductStatus,
        val priceSuggestions: Long?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val lastBringUpMyPost: LocalDateTime
    ) {
        constructor(product: Product, isSeller: Boolean): this(
            id = product.id,
            user = UserDto.Response(product.user),
            images = product.images?.map { it.url },
            title = product.title,
            content = product.content,
            price = product.price,
            negotiable = product.negotiable,
            category = product.category,
            forAge = product.forAge,
            location = product.location,
            rangeOfLocation = product.rangeOfLocation,
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
        val image: String?,
        val title: String,
        val price: Long,
        val location: String,
        val likes: Long,
        val chats: Long,
        val status: ProductStatus,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val lastBringUpMyPost: LocalDateTime
    ) {
        constructor(product: Product): this(
            id = product.id,
            user = UserDto.Response(product.user),
            image = if (product.images?.isNotEmpty() == true) product.images!![0].url else null,
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

    data class ProductSimpleResponseWithoutUser(
        val id: Long,
        val image: String?,
        val title: String,
        val price: Long,
        val location: String,
        val likes: Long,
        val chats: Long,
        val status: ProductStatus,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    ) {
        constructor(product: Product): this(
            id = product.id,
            image = product.images?.get(0)?.url,
            title = product.title,
            price = product.price,
            location = product.location,
            likes = product.likes,
            chats = product.chats,
            status = product.status,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }

    data class ProductPostRequest(
        val images: List<Long>? = null,
        @field:NotBlank
        val title: String,
        @field:Length(min = 1, max = 300)
        val content: String,
        @field:PositiveOrZero
        val price: Long,
        @field:BooleanFlag
        val negotiable: Boolean = true,
        @field:Range(min = 1, max = 17)
        val category: Int,
        val forAge: List<Int>? = null,
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
        val forAge: List<Int>? = null,
        @field:Range(min = 0, max = 3)
        val rangeOfLocation: Int? = null
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

    data class ProductStatusUpdateRequest(
        @field:NotBlank
        val action: String
    )
}