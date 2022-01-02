package waffle.team6.carrot.image.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ImageAccessByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.IMAGE_ACCESS_NOT_ALLOWED, detail)