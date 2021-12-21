package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class ProductAlreadySoldOutException(detail: String = ""):
        InvalidRequestException(ErrorType.PRODUCT_ALREADY_SOLD_OUT, detail)