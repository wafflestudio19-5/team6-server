package waffle.team6.carrot.purchaseOrders.model

import org.hibernate.validator.constraints.Range
import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.purchaseOrders.dto.PurchaseOrderDto
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.user.model.User
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "purchase_order")
class PurchaseOrder(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product", referencedColumnName = "id")
    val product: Product,

    @field:Range(min = 0, max = 10000000000)
    var suggestedPrice: Long? = null,

    var message: String? = null,

    var lastMessageTime: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    var status: PurchaseOrderStatus? = null
) : BaseTimeEntity() {
    constructor(user: User, product: Product, request: PurchaseOrderDto.PurchaseOrderPostRequest): this(
        user = user,
        product = product,
        suggestedPrice = request.suggestedPrice,
        message = request.message,
        lastMessageTime = if (request.message != null) LocalDateTime.now() else null
    )

    fun update(request: PurchaseOrderDto.PurchaseOrderUpdateRequest): PurchaseOrder {
        suggestedPrice = request.suggestedPrice
        message = request.message
        lastMessageTime = if (request.message != null) LocalDateTime.now() else null
        status = null
        return this
    }
}