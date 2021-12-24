package waffle.team6.carrot.user.dto


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
        val email: String?,
    ) {
        constructor(user: User): this(
            name = user.name,
            email = user.email,
        )
    }


    data class SignUpRequest(
        @field: NotBlank
        val name: String,

        @field:Email
        var email: String?,

        var phone: String?,

        @field: NotBlank
        @field: Size(min=8, max=16)
        val password: String,
    )


    data class UpdateProfileRequest(
        @field: Email
        var email: String?,

        var phone: String?,

        @field: NotBlank
        @field: Size(min=8, max=16)
        val password: String,
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