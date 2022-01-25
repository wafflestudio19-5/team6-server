package waffle.team6.carrot.user.dto


import waffle.team6.carrot.location.model.RangeOfLocation
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
        val activeLocation: String,
        val activeRangeOfLocation: RangeOfLocation,
        val activeLocationVerified: Boolean,
        val inactiveLocation: String?,
        val inactiveRangeOfLocation: RangeOfLocation?,
        val inactiveLocationVerified: Boolean,
        val isActive: Boolean,
    ) {
        constructor(user: User): this(
            name = user.name,
            nickname = user.nickname,
            email = user.email,
            phone = user.phone,
            imageUrl = user.imageUrl,
            activeLocation = user.activeLocation,
            activeRangeOfLocation = user.activeRangeOfLocation,
            activeLocationVerified = user.activeLocationVerified,
            inactiveLocation = user.inactiveLocation,
            inactiveRangeOfLocation = user.inactiveRangeOfLocation,
            inactiveLocationVerified = user.inactiveLocationVerified,
            isActive = user.isActive
        )
    }

    data class UserSimpleResponse(
        val name: String,
        val nickname: String,
        val isActive: Boolean
    ) {
        constructor(user: User): this(
            name = user.name,
            nickname = user.nickname,
            isActive = user.isActive,
        )
    }

    data class SignUpRequest(
        @field: NotBlank
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
    )

    data class UpdateProfileRequest(
        @field: Email
        val email: String?,

        val phone: String?,

        val nickname: String?,

        val location: String?,

        val rangeOfLocation: RangeOfLocation?,

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