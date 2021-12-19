package waffle.team6.carrot.user.model

import waffle.team6.carrot.BaseTimeEntity
import javax.persistence.*

@Entity
class SellerProfile(
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,
): BaseTimeEntity()