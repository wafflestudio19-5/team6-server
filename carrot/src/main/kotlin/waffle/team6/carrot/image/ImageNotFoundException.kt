package waffle.team6.carrot.image

import waffle.team6.global.common.exception.DataNotFoundException
import waffle.team6.global.common.exception.ErrorType

class ImageNotFoundException(detail: String = ""):
        DataNotFoundException(ErrorType.IMAGE_NOT_FOUND, detail)