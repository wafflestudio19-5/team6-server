package waffle.team6.carrot.product.model

import jdk.jfr.BooleanFlag
import waffle.team6.carrot.BaseTimeEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "product")
class Product (
    // TODO: User Information
    // @field:NotBlank
    // val user: User

    // TODO: Picture

    @field:NotBlank
    var title: String,

    // TODO: Contents

    @field:PositiveOrZero
    var price: Long,

    @field:BooleanFlag
    var negotiable: Boolean,

    @field:NotBlank
    var category: String,

    @field:NotBlank
    val location: String,

    @field:PositiveOrZero
    var hit: Long,

    @field:PositiveOrZero
    var like: Long,

    @field:PositiveOrZero
    var chat: Long
) : BaseTimeEntity()