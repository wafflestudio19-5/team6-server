package waffle.team6.carrot.user.dto


import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.user.model.User
import javax.persistence.Column
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class UserDto {
    data class Response(
        val name: String,
        val nickname: String,
        val email: String?,
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
        val rangeOfLocation: Int,
    )


    data class UpdateProfileRequest(
        @field: Email
        var email: String?,

        var phone: String?,
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