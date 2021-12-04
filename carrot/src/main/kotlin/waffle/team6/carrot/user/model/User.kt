package waffle.team6.carrot.user.model

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field: NotBlank
    val name: String,

    @field: NotBlank
    val password: String,

    val dateJoined: LocalDateTime = LocalDateTime.now(),

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var buyerProfile: BuyerProfile? = null,

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var sellerProfile: SellerProfile? = null,
)