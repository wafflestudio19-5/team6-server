package waffle.team6.carrot.image.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.ServerErrorException

class ImageLocalSaveFailException(detail: String = ""):
        ServerErrorException(ErrorType.IMAGE_LOCAL_SAVE_FAIL, detail)