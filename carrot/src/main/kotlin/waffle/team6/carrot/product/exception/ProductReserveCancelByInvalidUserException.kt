package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ProductReserveCancelByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PRODUCT_RESERVE_CANCEL_NOT_ALLOWED, detail)