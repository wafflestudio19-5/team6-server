package waffle.team6.carrot.user.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class InvalidStatusException(detail: String = ""):
        InvalidRequestException(ErrorType.INVALID_STATUS, detail)