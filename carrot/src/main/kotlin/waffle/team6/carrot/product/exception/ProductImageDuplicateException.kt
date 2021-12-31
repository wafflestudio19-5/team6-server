package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class ProductImageDuplicateException(detail: String = ""):
        InvalidRequestException(ErrorType.PRODUCT_IMAGE_ALREADY_REGISTERED_BY_ANOTHER_PRODUCT, detail)