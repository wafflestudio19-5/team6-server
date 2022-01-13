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
        val location: String,
        val rangeOfLocation: RangeOfLocation,
    ) {
        constructor(user: User): this(
            name = user.name,
            nickname = user.nickname,
            email = user.email,
            location = user.location,
            rangeOfLocation = user.rangeOfLocation,
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
}