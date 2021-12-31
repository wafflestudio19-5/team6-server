package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class InvalidForAgeValueException(detail: String = ""):
        InvalidRequestException(ErrorType.INVALID_CHILD_AGE_VALUE, detail)