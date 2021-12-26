package waffle.team6.carrot.user.model

import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.model.PurchaseRequest
import waffle.team6.carrot.user.dto.UserDto
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "user")
class User(
    @OneToMany(mappedBy = "user")
    val product: List<Product> = listOf(),

    @OneToMany(mappedBy = "user")
    var purchaseRequests: MutableList<PurchaseRequest> = mutableListOf(),

    @Column(unique = true)
    @field: NotBlank
    val name: String,

    @field: NotBlank
    var password: String,

    @field: Email
    var email: String?,

    var phone: String?,

    val dateJoined: LocalDateTime = LocalDateTime.now(),

    ): BaseTimeEntity() {
        fun modifyProfile(updateProfileRequest: UserDto.UpdateProfileRequest) {
            email = updateProfileRequest.email
            phone = updateProfileRequest.phone
        }
    }
