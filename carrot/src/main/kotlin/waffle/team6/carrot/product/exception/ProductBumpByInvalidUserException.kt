package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ProductBumpByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PRODUCT_BUMP_NOT_ALLOWED, detail)