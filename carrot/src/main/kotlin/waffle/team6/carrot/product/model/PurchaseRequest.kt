package waffle.team6.carrot.product.model

import waffle.team6.carrot.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "purchase_request")
class PurchaseRequest (
    @ManyToOne
    @JoinColumn(name = "buyer_profile", referencedColumnName = "id")
    val buyerProfile: BuyerProfile,

    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "id")
    val product: Product
    ) : BaseTimeEntity() {

}