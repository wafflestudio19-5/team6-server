package waffle.team6.carrot.user.dto


import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.user.model.KakaoStatus
import waffle.team6.carrot.user.model.NoAtInUserName
import waffle.team6.carrot.user.model.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class UserDto {
    data class Response(
        val name: String,
        val nickname: String,
        val email: String,
        val phone: String,
        val imageUrl: String?,
        val firstLocation: String,
        val firstRangeOfLocation: RangeOfLocation,
        val firstLocationVerified: Boolean,
        val secondLocation: String?,
        val secondRangeOfLocation: RangeOfLocation?,
        val secondLocationVerified: Boolean,
        val isFirstLocationActive: Boolean,
        val isActive: Boolean,
        val kakaoStatus: KakaoStatus?,
    ) {
        constructor(user: User): this(
            name = user.name,
            nickname = user.nickname,
            email = user.email,
            phone = user.phone,
            imageUrl = user.imageUrl,
            firstLocation = user.firstLocation,
            firstRangeOfLocation = user.firstRangeOfLocation,
            firstLocationVerified = user.firstLocationVerified,
            secondLocation = user.secondLocation,
            secondRangeOfLocation = user.secondRangeOfLocation,
            secondLocationVerified = user.secondLocationVerified,
            isFirstLocationActive = user.isFirstLocationActive,
            isActive = user.isActive,
            kakaoStatus = user.kakaoStatus,
        )
    }

    data class UserSimpleResponse(
        val id: Long,
        val name: String,
        val nickname: String,
        val isActive: Boolean
    ) {
        constructor(user: User): this(
            id = user.id,
            name = user.name,
            nickname = user.nickname,
            isActive = user.isActive,
        )
    }

    data class SignUpRequest(
        @field: NotBlank
        @field: NoAtInUserName
        val name: String,

        @field: NotBlank
        val nickname: String,

        @field: Email
        @field: NotBlank
        val email: String,

        // TODO phone number form validation
        @field: NotBlank
        val phone: String,

        @field: NotBlank
        @field: Size(min=8, max=16)
        val password: String,

        val location: String,

        @field: NotNull
        val rangeOfLocation: RangeOfLocation,

        val kakaoStatus: KakaoStatus? = null,
    )

    data class KakaoSignUpRequest(
        @field: NotBlank
        @field: NoAtInUserName
        val name: String,

        @field: NotBlank
        val nickname: String,

        @field: Email
        @field: NotBlank
        val email: String,

        // TODO phone number form validation
        @field: NotBlank
        val phone: String,

        @field: NotBlank
        @field: Size(min=8, max=16)
        val password: String,

        val location: String,

        @field: NotNull
        val rangeOfLocation: RangeOfLocation,

        val kakaoStatus: KakaoStatus? = null,
    )

    data class UpdateProfileRequest(
        @field: Email
        val email: String?,

        val phone: String?,

        val nickname: String?,

        val imageUrl: String?,
    )

    data class UpdatePasswordRequest(
        @field: NotBlank
        @field: Size(min=8, max=16)
        val currentPassword: String,

        @field: NotBlank
        @field: Size(min=8, max=16)
        val newPassword: String,
    )

    data class UpdateLocationRequest(
        val location: String,
        val rangeOfLocation: RangeOfLocation
    )
}