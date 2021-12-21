package waffle.team6.carrot.product.model

import jdk.jfr.BooleanFlag
import org.hibernate.validator.constraints.Length
import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.user.model.User
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "products")
class Product (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    val user: User,

    @ElementCollection
    var images: List<String> = listOf(),

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
    var likes: Long,

    @field:PositiveOrZero
    var chat: Long,

    @field:PositiveOrZero
    var priceSuggestion: Long,

    @Enumerated(EnumType.STRING)
    var status: Status,

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "product")
     var purchaseRequest: MutableList<PurchaseRequest> = mutableListOf<PurchaseRequest>(),

    ) : BaseTimeEntity() {
    constructor(user: User, productPostRequest: ProductDto.PostRequest): this(
        user = user,
        images = productPostRequest.images,
        title = productPostRequest.title,
        content = productPostRequest.content,
        price = productPostRequest.price,
        negotiable = productPostRequest.negotiable?: true,
        category = productPostRequest.category,
        location = productPostRequest.location,
        hit = 1,
        likes = 0,
        chat = 0,
        priceSuggestion = 0,
        status = Status.FOR_SALE,
    )

    fun modify(productModifyRequest: ProductDto.ModifyRequest): Product{
        images = productModifyRequest.images
        title = productModifyRequest.title
        content = productModifyRequest.content
        price = productModifyRequest.price
        negotiable = productModifyRequest.negotiable
        category = productModifyRequest.category
        status = Status.FOR_SALE
        return this
    }
}