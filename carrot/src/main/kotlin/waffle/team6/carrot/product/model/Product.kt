package waffle.team6.carrot.product.model

import jdk.jfr.BooleanFlag
import org.hibernate.validator.constraints.Length
import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.image.model.Image
import waffle.team6.carrot.location.model.AdjacentLocation
import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.product.dto.ProductDto
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

    @OneToMany(cascade = [CascadeType.ALL])
    var images: MutableList<Image> = mutableListOf(),

    @field:NotBlank
    var title: String,

    @field:Length(min = 1, max = 300)
    var content: String,

    @field:PositiveOrZero
    var price: Long,

    @field:BooleanFlag
    var negotiable: Boolean,

    @Enumerated(EnumType.STRING)
    var category: Category,

    @Enumerated(EnumType.STRING)
    var forAge: ForAge?,

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
    var status: Status,

    @field:BooleanFlag
    var hidden: Boolean,

    var lastBringUpMyPost: LocalDateTime = LocalDateTime.now(),

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "product")
     var purchaseRequests: MutableList<PurchaseRequest> = mutableListOf(),

    ) : BaseTimeEntity() {
    constructor(
        user: User,
        images: MutableList<Image>,
        adjacentLocations: List<String>,
        productPostRequest: ProductDto.ProductPostRequest
    ): this(
        user = user,
        images = images,
        title = productPostRequest.title,
        content = productPostRequest.content,
        price = productPostRequest.price,
        negotiable = productPostRequest.negotiable?: true,
        category = Category.from(productPostRequest.category),
        forAge = if (productPostRequest.category == 4) productPostRequest.forAge?.let { ForAge.from(it) } else null,
        location = user.location,
        rangeOfLocation = RangeOfLocation.from(productPostRequest.rangeOfLocation),
        adjacentLocations = adjacentLocations,
        hit = 1,
        likes = 0,
        chats = 0,
        priceSuggestions = 0,
        status = Status.FOR_SALE,
        hidden = false
    )
}