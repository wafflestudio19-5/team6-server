package waffle.team6.carrot.user.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class UserDto {
    data class Response(
        val name: String,
    )


    data class SignUpRequest(
        @field: NotBlank
        val name: String,

        @field: NotBlank
        @field: Size(min=8, max=16)
        val password: String,
    )

}