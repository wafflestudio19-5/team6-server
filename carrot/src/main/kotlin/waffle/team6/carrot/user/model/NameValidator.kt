package waffle.team6.carrot.user.model

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NameValidator: ConstraintValidator<NoAtInUserName, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value != null) {
            for (character in value) {
                if (character == '@')
                    return false
            }
        } else return false
        return true
    }
}