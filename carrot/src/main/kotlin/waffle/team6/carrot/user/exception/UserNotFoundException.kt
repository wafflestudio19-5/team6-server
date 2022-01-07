package waffle.team6.carrot.user.exception

import waffle.team6.global.common.exception.DataNotFoundException
import waffle.team6.global.common.exception.ErrorType

class UserNotFoundException(detail: String = ""):
    DataNotFoundException(ErrorType.USER_NOT_FOUND)