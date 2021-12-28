package waffle.team6.carrot.product.model

import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.user.model.User
import javax.persistence.*

@Entity
@Table(name = "category_of_interest")
class CategoryOfInterest (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    val user: User,

    val category: Category,
) :BaseTimeEntity()