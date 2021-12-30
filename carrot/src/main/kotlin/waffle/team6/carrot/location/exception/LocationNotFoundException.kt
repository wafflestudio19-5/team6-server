package waffle.team6.carrot.location.exception

import waffle.team6.global.common.exception.DataNotFoundException
import waffle.team6.global.common.exception.ErrorType

class LocationNotFoundException(detail: String = ""):
        DataNotFoundException(ErrorType.LOCATION_NOT_FOUND, detail)