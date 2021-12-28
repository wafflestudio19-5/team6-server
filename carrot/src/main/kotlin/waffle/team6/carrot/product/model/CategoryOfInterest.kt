package waffle.team6.carrot.product.model

import waffle.team6.carrot.user.model.User
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

class CategoryOfInterest (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    val user: User,

    val category: Category,
)