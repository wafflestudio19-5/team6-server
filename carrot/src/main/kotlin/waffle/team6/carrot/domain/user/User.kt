package waffle.team6.carrot.domain.user

import waffle.team6.global.auth.SocialType
import javax.persistence.*

@Entity
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(value = EnumType.STRING)
    val socialType: SocialType,

    val username: String,

    val password: String,
)
