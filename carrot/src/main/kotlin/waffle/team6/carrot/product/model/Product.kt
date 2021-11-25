package waffle.team6.carrot.product.model

import jdk.jfr.BooleanFlag
import org.hibernate.validator.constraints.Length
import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.product.dto.ProductDto
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "product")
class Product (
    // TODO: User Information
    // @field:NotBlank
    // val user: User

    // TODO: Picture

    @field:NotBlank
    var title: String,

    @field:Length(min = 1, max = 300)
    var content: String,

    @field:PositiveOrZero
    var price: Long,

    @field:BooleanFlag
    var negotiable: Boolean,

    @field:NotBlank
    var category: String,

    @field:NotBlank
    val location: String,

    @field:PositiveOrZero
    var hit: Long,

    @field:PositiveOrZero
    var like: Long,

    @field:PositiveOrZero
    var chat: Long,

    @Enumerated(EnumType.STRING)
    var status: Status,

) : BaseTimeEntity() {
    constructor(productPostRequest: ProductDto.PostRequest): this(
        // user = ...
        // picture = productPostRequest.picture,
        title = productPostRequest.title,
        content = productPostRequest.content,
        price = productPostRequest.price,
        negotiable = productPostRequest.negotiable?: true,
        category = productPostRequest.category,
        location = productPostRequest.location,
        hit = 1,
        like = 0,
        chat = 0,
        status = Status.FOR_SALE,
    )

    fun modify(productModifyRequest: ProductDto.ModifyRequest): Product{
        // picture = ...
        title = productModifyRequest.title
        content = productModifyRequest.content
        price = productModifyRequest.price
        negotiable = productModifyRequest.negotiable
        category = productModifyRequest.category
        updatedAt = LocalDateTime.now()
        return this
    }
}