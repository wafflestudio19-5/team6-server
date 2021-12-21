package waffle.team6.carrot.image.model

import waffle.team6.carrot.BaseTimeEntity
import javax.persistence.*

@Table
@Entity
class Image(
    var url: String
): BaseTimeEntity()