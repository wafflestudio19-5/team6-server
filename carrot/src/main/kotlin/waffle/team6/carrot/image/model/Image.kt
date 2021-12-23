package waffle.team6.carrot.image.model

import waffle.team6.carrot.BaseTimeEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Table
@Entity
class Image(
    @field:NotBlank
    var fileName: String,

    @field:NotBlank
    var contentType: String,

    @field:NotNull
    val userId: Long
): BaseTimeEntity()