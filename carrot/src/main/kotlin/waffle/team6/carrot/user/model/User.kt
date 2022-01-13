package waffle.team6.carrot.user.model

import jdk.jfr.BooleanFlag
import waffle.team6.carrot.BaseTimeEntity
import waffle.team6.carrot.product.model.CategoryOfInterest
import waffle.team6.carrot.product.model.Like
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.purchaseOrders.model.PurchaseOrder
import waffle.team6.carrot.user.dto.UserDto
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import waffle.team6.carrot.location.model.RangeOfLocation
import javax.validation.constraints.NotNull

@Entity
@Table(name = "user")
class User(
    @OneToMany(mappedBy = "user")
    var products: MutableList<Product> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var purchaseOrders: MutableList<PurchaseOrder> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var likes: MutableList<Like> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var categoriesOfInterest: MutableList<CategoryOfInterest> = mutableListOf(),

    @Column(unique = true)
    @field: NotBlank
    val name: String,

    @field: NotBlank
    var nickname: String,

    @field: Email
    var email: String,

    var phone: String,

    @field: NotBlank
    var password: String,

    @field: NotBlank
    var location: String,

    @field: NotNull
    var rangeOfLocation: RangeOfLocation,

    var imageUrl: String?,

    @field: BooleanFlag
    var isActive: Boolean,

    @ElementCollection
    var myPhrases: MutableList<String> = mutableListOf(),
): BaseTimeEntity() {
    constructor(signUpRequest: UserDto.SignUpRequest, encodedPassword: String): this(
        name = signUpRequest.name,
        nickname = signUpRequest.nickname,
        password = encodedPassword,
        email = signUpRequest.email,
        phone = signUpRequest.phone,
        location = signUpRequest.location,
        rangeOfLocation = signUpRequest.rangeOfLocation,
        imageUrl = null,
        isActive = true
    )

    fun modifyProfile(updateProfileRequest: UserDto.UpdateProfileRequest): User {
        email = updateProfileRequest.email ?: email
        phone = updateProfileRequest.phone ?: phone
        nickname = updateProfileRequest.nickname ?: nickname
        location = updateProfileRequest.location ?: location
        rangeOfLocation = updateProfileRequest.rangeOfLocation ?: rangeOfLocation
        imageUrl = updateProfileRequest.imageUrl ?: imageUrl
        return this
        }
}
