package waffle.team6.carrot.user.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class SellerProfile(
    @Id @GeneratedValue
    val id: Long = 0,
) {
}