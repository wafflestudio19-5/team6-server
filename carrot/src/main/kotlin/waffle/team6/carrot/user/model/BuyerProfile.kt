package waffle.team6.carrot.user.model

import javax.persistence.*

@Entity
class BuyerProfile(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

)