package waffle.team6.carrot.image.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class ImageInvalidContentTypeException(detail: String = ""):
        InvalidRequestException(ErrorType.IMAGE_INVALID_CONTENT_TYPE, detail)