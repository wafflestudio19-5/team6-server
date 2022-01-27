package waffle.team6.carrot.user.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class UserKakaoLoginException(detail: String = "Kakao login REST API error"):
    InvalidRequestException(ErrorType.KAKAO_CLIENT_ERROR, detail)