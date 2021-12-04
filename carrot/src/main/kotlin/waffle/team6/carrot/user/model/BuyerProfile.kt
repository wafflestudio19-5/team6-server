package waffle.team6.carrot.user.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class BuyerProfile(
    @Id @GeneratedValue
    val id: Long = 0,

    @OneToOne
    val user: User,

)