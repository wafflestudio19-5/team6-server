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
    @ManyToOne
     @JoinColumn(name = "seller_profile", referencedColumnName = "id")
     val sellerProfile: SellerProfile,

    @ElementCollection
    var images: List<String> = listOf(),

    @field:NotBlank
    @Column(name = "title")
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

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "product")
    var like: MutableList<Like> = mutableListOf<Like>(),

    @field:PositiveOrZero
    var chat: Long,

    @Enumerated(EnumType.STRING)
    var status: Status,

    @OneToOne
     @JoinColumn(name = "purchase_record", referencedColumnName = "id")
     var purchaseRecord: PurchaseRecord? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "product")
     var purchaseRequest: MutableList<PurchaseRequest> = mutableListOf<PurchaseRequest>(),

    ) : BaseTimeEntity() {
    constructor(productPostRequest: ProductDto.PostRequest): this(
        // user = ...
        images = productPostRequest.images,
        title = productPostRequest.title,
        content = productPostRequest.content,
        price = productPostRequest.price,
        negotiable = productPostRequest.negotiable?: true,
        category = productPostRequest.category,
        location = productPostRequest.location,
        hit = 1,
        chat = 0,
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
        updatedAt = LocalDateTime.now()
        return this
    }
}