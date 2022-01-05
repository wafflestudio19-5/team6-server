package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ProductStatusChangeByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PRODUCT_STATUS_CHANGE_NOT_ALLOWED, detail)