package waffle.team6.carrot.image.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ImageDeleteByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.IMAGE_DELETE_NOT_ALLOWED, detail)