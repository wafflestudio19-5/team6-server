package waffle.team6.carrot.location.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "location")
class Location (
    @Id
    val code: Long,

    @field:NotBlank
    val name: String,

    @OneToMany
    val levelZero: List<AdjacentLocation> = listOf(),

    @OneToMany
    val levelOne: List<AdjacentLocation> = listOf(),

    @OneToMany
    val levelTwo: List<AdjacentLocation> = listOf(),

    @OneToMany
    val levelThree: List<AdjacentLocation> = listOf()
)