package waffle.team6.carrot.product.model

import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.user.model.User
import javax.persistence.*

@Entity
@Table(name = "like")
class Like (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product", referencedColumnName = "id")
    val product: Product
    ) : BaseTimeEntity()