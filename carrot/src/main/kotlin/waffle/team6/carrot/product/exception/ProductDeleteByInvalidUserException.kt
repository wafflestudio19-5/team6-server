package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ProductDeleteByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PRODUCT_DELETE_NOT_ALLOWED, detail)