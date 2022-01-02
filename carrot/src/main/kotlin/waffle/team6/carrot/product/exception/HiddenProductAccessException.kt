package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class HiddenProductAccessException(detail: String = ""):
        NotAllowedException(ErrorType.HIDDEN_PRODUCT_RETRIEVE_NOT_ALLOWED, detail)