package waffle.team6.carrot.user.model

import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.product.model.Product
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "user")
class User(
    @OneToMany(mappedBy = "user")
    val products: List<Product> = listOf(),

    //TODO: purchase requests

    @Column(unique = true)
    @field: NotBlank
    val name: String,

    @field: NotBlank
    val password: String,

    @field: Email
    var email: String?,

    var phone: String?,

    val dateJoined: LocalDateTime = LocalDateTime.now(),


    ): BaseTimeEntity()
