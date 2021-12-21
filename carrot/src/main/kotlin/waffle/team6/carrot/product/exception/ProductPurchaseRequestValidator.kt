package waffle.team6.carrot.product.exception

import waffle.team6.carrot.user.model.User
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ProductPurchaseRequestValidator : ConstraintValidator<ValidProductPurchaseRequestAccess, Any> {
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        TODO("Not yet implemented")
    }
}