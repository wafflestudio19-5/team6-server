package waffle.team6.carrot.product.model

import jdk.jfr.BooleanFlag
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.image.model.Image
import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.purchaseOrders.model.PurchaseOrder
import waffle.team6.carrot.user.model.User
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "product")
class Product (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    val user: User,

    @ElementCollection
    var imageUrls: List<String> = listOf(),

    @field:Length(min = 1, max = 50)
    var title: String,

    @field:Length(min = 1, max = 300)
    var content: String,

    @field:Range(min = 0, max = 10000000000)
    var price: Long,

    @field:BooleanFlag
    var negotiable: Boolean,

    @Enumerated(EnumType.STRING)
    var category: Category,

    @ElementCollection
    var forAge: MutableList<ForAge>? = null,

    @field:NotBlank
    val location: String,

    @Enumerated(EnumType.STRING)
    var rangeOfLocation: RangeOfLocation,

    @ElementCollection
    var adjacentLocations: List<String> = listOf(),

    @field:PositiveOrZero
    var hit: Long,

    @field:PositiveOrZero
    var likes: Long,

    @field:PositiveOrZero
    var chats: Long,

    @field:PositiveOrZero
    var priceSuggestions: Long,

    @Enumerated(EnumType.STRING)
    var status: ProductStatus,

    @field:BooleanFlag
    var hidden: Boolean,

    var lastBringUpMyPost: LocalDateTime = LocalDateTime.now(),

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "product")
    var purchaseOrders: MutableList<PurchaseOrder> = mutableListOf(),

    ) : BaseTimeEntity() {
    constructor(
        user: User,
        adjacentLocations: List<String>,
        productPostRequest: ProductDto.ProductPostRequest
    ): this(
        user = user,
        imageUrls = productPostRequest.imageUrls,
        title = productPostRequest.title,
        content = productPostRequest.content,
        price = productPostRequest.price,
        negotiable = productPostRequest.negotiable,
        category = Category.from(productPostRequest.category),
        forAge = (if (productPostRequest.category == 4) productPostRequest.forAge
            ?.map { ForAge.from(it) } else null) as MutableList<ForAge>?,
        location = user.location,
        rangeOfLocation = RangeOfLocation.from(productPostRequest.rangeOfLocation),
        adjacentLocations = adjacentLocations,
        hit = 1,
        likes = 0,
        chats = 0,
        priceSuggestions = 0,
        status = ProductStatus.FOR_SALE,
        hidden = false
    )

    fun modify(productPatchRequest: ProductDto.ProductUpdateRequest, adjacentLocationsToChange: List<String>?) {
        imageUrls = productPatchRequest.imageUrls ?: imageUrls
        title = productPatchRequest.title ?: title
        content = productPatchRequest.content ?: content
        price = productPatchRequest.price ?: price
        negotiable = productPatchRequest.negotiable ?: negotiable
        category = productPatchRequest.category?.let { Category.from(it) } ?: category
        forAge = (if (productPatchRequest.category == 4) productPatchRequest.forAge
            ?.map { ForAge.from(it) } else null) as MutableList<ForAge>
        adjacentLocations = adjacentLocationsToChange ?: adjacentLocations
        rangeOfLocation = productPatchRequest.rangeOfLocation?.let { RangeOfLocation.from(it) } ?: rangeOfLocation
    }
}