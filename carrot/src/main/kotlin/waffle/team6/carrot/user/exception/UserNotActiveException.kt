package waffle.team6.carrot.user.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class UserNotActiveException(detail: String = ""):
        InvalidRequestException(ErrorType.USER_NOT_ACTIVE, detail)