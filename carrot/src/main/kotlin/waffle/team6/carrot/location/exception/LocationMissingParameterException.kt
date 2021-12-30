package waffle.team6.carrot.location.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class LocationMissingParameterException(detail: String = ""):
        InvalidRequestException(ErrorType.LOCATION_NAME_AND_CODE_MISSING, detail)