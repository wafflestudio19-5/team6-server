package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ProductShowByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PRODUCT_SHOW_NOT_ALLOWED, detail)