package waffle.team6.carrot.product.model

import jdk.jfr.BooleanFlag
import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.product.dto.PurchaseRequestDto
import waffle.team6.carrot.user.model.User
import java.time.LocalDateTime
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
    var suggestedPrice: Long? = null,

    var message: String? = null,

    var lastMessageTime: LocalDateTime? = null,

    @field:BooleanFlag
    var accepted: Boolean? = null
    ) : BaseTimeEntity() {
    constructor(user: User, product: Product, request: PurchaseRequestDto.PurchaseRequest): this(
        user = user,
        product = product,
        suggestedPrice = request.suggestedPrice,
        message = request.message,
        lastMessageTime = if (request.message != null) LocalDateTime.now() else null
    )

    fun update(request: PurchaseRequestDto.PurchaseRequest): PurchaseRequest {
        suggestedPrice = request.suggestedPrice
        message = request.message
        lastMessageTime = if (request.message != null) LocalDateTime.now() else null
        accepted = null
        return this
    }
}