package waffle.team6.carrot.user.dto

import waffle.team6.carrot.user.model.User
import javax.persistence.Column
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class UserDto {
    data class Response(
        val name: String,
    ) {
        constructor(user: User): this(
            user.name
        )
    }


    data class SignUpRequest(
        @field: NotBlank
        val name: String,

        @field:NotBlank
        @field:Email
        var email: String,

        @field: NotBlank
        @field: Size(min=8, max=16)
        val password: String,
    )

}