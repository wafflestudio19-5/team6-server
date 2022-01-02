package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ProductHideByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PRODUCT_HIDE_NOT_ALLOWED, detail)