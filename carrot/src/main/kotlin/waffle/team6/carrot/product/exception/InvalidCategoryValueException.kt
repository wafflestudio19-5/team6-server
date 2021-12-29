package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class InvalidCategoryValueException(detail: String = ""):
        InvalidRequestException(ErrorType.INVALID_CATEGORY_VALUE, detail)