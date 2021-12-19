package waffle.team6.carrot.user.model

import waffle.team6.carrot.BaseTimeEntity
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
class User(
    @Column(unique = true)
    @field: NotBlank
    val name: String,

    @field: NotBlank
    val password: String,

    @field: Email
    var email: String?,

    var phone: String?,

    val dateJoined: LocalDateTime = LocalDateTime.now(),

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var buyerProfile: BuyerProfile? = null,

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var sellerProfile: SellerProfile? = null,
): BaseTimeEntity()