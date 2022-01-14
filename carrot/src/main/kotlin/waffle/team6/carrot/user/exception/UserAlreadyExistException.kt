package waffle.team6.carrot.user.exception

import waffle.team6.global.common.exception.ConflictException
import waffle.team6.global.common.exception.ErrorType

class UserAlreadyExistException(detail: String = ""):
    ConflictException(ErrorType.USER_CONFLICT, detail)