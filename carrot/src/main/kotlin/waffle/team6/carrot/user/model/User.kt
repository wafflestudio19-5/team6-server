package waffle.team6.carrot.user.model

import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.product.model.CategoryOfInterest
import waffle.team6.carrot.product.model.Like
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.model.PurchaseRequest
import waffle.team6.carrot.user.dto.UserDto
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Range

@Entity
@Table(name = "user")
class User(
    @OneToMany(mappedBy = "user")
    var products: MutableList<Product> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var purchaseRequests: MutableList<PurchaseRequest> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var likes: MutableList<Like> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var categoriesOfInterest: MutableList<CategoryOfInterest> = mutableListOf(),

    @Column(unique = true)
    @field: NotBlank
    val name: String,

    @field: NotBlank
    val nickname: String,

    @field: Email
    var email: String?,

    var phone: String?,

    @field: NotBlank
    var password: String,

    @field: NotBlank
    var location: String,

    @field: Range(min=0, max=3)
    var rangeOfLocation: Int, // 0, 1, 2, 3

): BaseTimeEntity() {
        fun modifyProfile(updateProfileRequest: UserDto.UpdateProfileRequest) {
            email = updateProfileRequest.email
            phone = updateProfileRequest.phone
        }
    }
