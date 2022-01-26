package waffle.team6.carrot.user.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

class PhraseDto {
    data class PhrasePostRequest(
        @field: NotBlank
        @field: Length(min=1, max=30)
        val phrase: String
    )

    data class PhraseResponse(
        val phrases: List<String>
    )
}