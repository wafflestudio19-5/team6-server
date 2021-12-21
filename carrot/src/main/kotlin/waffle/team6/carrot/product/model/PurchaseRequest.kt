package waffle.team6.carrot.product.model

import jdk.jfr.BooleanFlag
import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.product.dto.PurchaseRequestDto
import waffle.team6.carrot.user.model.User
import javax.persistence.*
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "purchase_request")
class PurchaseRequest (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product", referencedColumnName = "id")
    val product: Product,

    @field:PositiveOrZero
    val suggestedPrice: Long? = null,

    @field:BooleanFlag
    var accepted: Boolean = false
    ) : BaseTimeEntity() {
    constructor(user: User, product: Product, request: PurchaseRequestDto.Request): this(
        user = user,
        product = product,
        suggestedPrice = request.suggestedPrice
    )
}