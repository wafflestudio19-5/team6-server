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
    var firstLocation: String,

    @field: NotNull
    var firstRangeOfLocation: RangeOfLocation,

    @field: BooleanFlag
    var firstLocationVerified: Boolean,

    var secondLocation: String?,

    var secondRangeOfLocation: RangeOfLocation?,

    @field: BooleanFlag
    var secondLocationVerified: Boolean,

    @field: BooleanFlag
    var isFirstLocationActive: Boolean,

    var imageUrl: String?,

    @field: BooleanFlag
    var isActive: Boolean,

    @ElementCollection
    var myPhrases: MutableList<String> = mutableListOf(),

    var kakaoStatus: KakaoStatus? = null,

): BaseTimeEntity() {
    constructor(signUpRequest: UserDto.SignUpRequest, encodedPassword: String): this(
        name = signUpRequest.name,
        nickname = signUpRequest.nickname,
        password = encodedPassword,
        email = signUpRequest.email,
        phone = signUpRequest.phone,
        firstLocation = signUpRequest.location,
        firstRangeOfLocation = signUpRequest.rangeOfLocation,
        firstLocationVerified = false,
        secondLocation = null,
        secondRangeOfLocation = null,
        secondLocationVerified = false,
        isFirstLocationActive = true,
        imageUrl = null,
        isActive = true,
        kakaoStatus = signUpRequest.kakaoStatus
    )

    constructor(signUpRequest: UserDto.KakaoSignUpRequest, encodedPassword: String): this(
        name = signUpRequest.name,
        nickname = signUpRequest.nickname,
        password = encodedPassword,
        email = signUpRequest.email,
        phone = signUpRequest.phone,
        firstLocation = signUpRequest.location,
        firstRangeOfLocation = signUpRequest.rangeOfLocation,
        firstLocationVerified = false,
        secondLocation = null,
        secondRangeOfLocation = null,
        secondLocationVerified = false,
        isFirstLocationActive = true,
        imageUrl = null,
        isActive = true,
        kakaoStatus = signUpRequest.kakaoStatus
    )

    fun modifyProfile(updateProfileRequest: UserDto.UpdateProfileRequest): User {
        email = updateProfileRequest.email ?: email
        phone = updateProfileRequest.phone ?: phone
        nickname = updateProfileRequest.nickname ?: nickname
        imageUrl = updateProfileRequest.imageUrl ?: imageUrl
        if (kakaoStatus == KakaoStatus.INVALID) {
            kakaoStatus = KakaoStatus.VALID
        }
        return this
    }

    fun deleteImage(): User {
        imageUrl = null
        return this
    }

    fun verifyLocation(): User {
        if (isFirstLocationActive) {
            firstLocationVerified = true
        } else {
            secondLocationVerified = true
        }
        return this
    }

    fun addLocation(updateLocationRequest: UserDto.UpdateLocationRequest): User {
        secondLocation = updateLocationRequest.location
        secondRangeOfLocation = updateLocationRequest.rangeOfLocation
        secondLocationVerified = false
        isFirstLocationActive = false
        return this
    }

    fun deleteLocation(isFirstSelected: Boolean): User {
        if (isFirstSelected && secondLocation != null) {
            firstLocation = secondLocation!!
            firstRangeOfLocation = secondRangeOfLocation!!
            firstLocationVerified = secondLocationVerified
        }
        secondLocation = null
        secondRangeOfLocation = null
        secondLocationVerified = false
        return this
    }

    fun updateLocation(updateLocationRequest: UserDto.UpdateLocationRequest): User {
        if (isFirstLocationActive) {
            firstLocation = updateLocationRequest.location
            firstRangeOfLocation = updateLocationRequest.rangeOfLocation
            firstLocationVerified = false
        } else {
            secondLocation = updateLocationRequest.location
            secondRangeOfLocation = updateLocationRequest.rangeOfLocation
            secondLocationVerified = false
        }
        return this
    }

    fun changeLocation(): User {
        isFirstLocationActive = !(isFirstLocationActive && secondLocation != null)
        return this
    }
}
