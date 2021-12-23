package waffle.team6.carrot.image.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ImageUpdateByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.IMAGE_UPDATE_NOT_ALLOWED, detail)