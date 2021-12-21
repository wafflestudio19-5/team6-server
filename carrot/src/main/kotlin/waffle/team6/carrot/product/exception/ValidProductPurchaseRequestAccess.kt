package waffle.team6.carrot.product.exception

import javax.validation.Constraint

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ProductPurchaseRequestValidator::class])
annotation class ValidProductPurchaseRequestAccess(
    val message: String = "{}",

)
