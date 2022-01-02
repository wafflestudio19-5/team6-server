package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class ProductEarlyBumpException(detail: String = ""):
        InvalidRequestException(ErrorType.PRODUCT_BUMP_ONLY_ONCE_IN_A_DAY, detail)