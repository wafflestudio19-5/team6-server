package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ProductReserveByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PRODUCT_RESERVE_NOT_ALLOWED, detail)