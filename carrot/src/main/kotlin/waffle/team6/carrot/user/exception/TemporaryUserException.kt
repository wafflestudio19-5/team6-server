package waffle.team6.carrot.user.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class TemporaryUserException(detail: String = ""):
    NotAllowedException(ErrorType.USER_KAKAO_NOT_ALLOWED, detail)
