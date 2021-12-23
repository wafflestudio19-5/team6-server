package waffle.team6.carrot.image.model

import waffle.team6.carrot.BaseTimeEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Table
@Entity
class Image(
    @field:NotBlank
    var fileName: String,

    @field:NotBlank
    var contentType: String
): BaseTimeEntity()