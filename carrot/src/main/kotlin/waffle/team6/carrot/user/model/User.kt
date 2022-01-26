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
    var activeLocation: String,

    @field: NotNull
    var activeRangeOfLocation: RangeOfLocation,

    @field: BooleanFlag
    var activeLocationVerified: Boolean,

    var inactiveLocation: String?,

    var inactiveRangeOfLocation: RangeOfLocation?,

    @field: BooleanFlag
    var inactiveLocationVerified: Boolean,

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
        activeLocation = signUpRequest.location,
        activeRangeOfLocation = signUpRequest.rangeOfLocation,
        activeLocationVerified = false,
        inactiveLocation = null,
        inactiveRangeOfLocation = null,
        inactiveLocationVerified = false,
        imageUrl = null,
        isActive = true
    )

    fun modifyProfile(updateProfileRequest: UserDto.UpdateProfileRequest): User {
        email = updateProfileRequest.email ?: email
        phone = updateProfileRequest.phone ?: phone
        nickname = updateProfileRequest.nickname ?: nickname
        imageUrl = updateProfileRequest.imageUrl ?: imageUrl
        return this
    }

    fun deleteImage(): User {
        imageUrl = null
        return this
    }

    fun verifyLocation(): User {
        activeLocationVerified = true
        return this
    }

    fun addLocation(updateLocationRequest: UserDto.UpdateLocationRequest): User {
        inactiveLocation = activeLocation
        inactiveRangeOfLocation = activeRangeOfLocation
        inactiveLocationVerified = activeLocationVerified
        activeLocation = updateLocationRequest.location
        activeRangeOfLocation = updateLocationRequest.rangeOfLocation
        activeLocationVerified = false
        return this
    }

    fun deleteLocation(): User {
        inactiveLocation = null
        inactiveRangeOfLocation = null
        inactiveLocationVerified = false
        return this
    }

    fun updateLocation(updateLocationRequest: UserDto.UpdateLocationRequest): User {
        activeLocation = updateLocationRequest.location
        activeRangeOfLocation = updateLocationRequest.rangeOfLocation
        activeLocationVerified = false
        return this
    }

    fun changeLocation(): User {
        if (inactiveLocation == null) return this
        val temporaryLocation = activeLocation
        val temporaryRangeOfLocation = activeRangeOfLocation
        val temporaryLocationVerified = activeLocationVerified
        activeLocation = inactiveLocation!!
        activeRangeOfLocation = inactiveRangeOfLocation!!
        activeLocationVerified = inactiveLocationVerified
        inactiveLocation = temporaryLocation
        inactiveRangeOfLocation = temporaryRangeOfLocation
        inactiveLocationVerified = temporaryLocationVerified
        return this
    }
}
