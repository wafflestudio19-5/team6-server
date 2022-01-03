package waffle.team6.carrot.location.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class InvalidRangeOfLocationLevelException(detail: String = ""):
        InvalidRequestException(ErrorType.INVALID_RANGE_OF_LOCATION_LEVEL, detail)