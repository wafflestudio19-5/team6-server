package waffle.team6.carrot.user.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class UserLocationNotVerifiedException(detail: String = ""):
        NotAllowedException(ErrorType.USER_LOCATION_NOT_VERIFIED, detail)