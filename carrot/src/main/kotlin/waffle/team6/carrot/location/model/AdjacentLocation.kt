package waffle.team6.carrot.location.model

import javax.persistence.*

@Entity
@Table(name = "adjacent_location")
class AdjacentLocation (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String
)