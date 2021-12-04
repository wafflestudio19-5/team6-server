package waffle.team6.carrot.user.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.validation.constraints.NotBlank

@Entity
class User(
    @Id @GeneratedValue
    val id: Long = 0,

    @field: NotBlank
    val name: String?,

    @field: NotBlank
    val password: String?,

    @OneToOne
    val buyerProfile: BuyerProfile,

    @OneToOne
    val sellerProfile: SellerProfile,
)