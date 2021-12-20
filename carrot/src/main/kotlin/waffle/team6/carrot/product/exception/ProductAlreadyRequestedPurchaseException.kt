package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class ProductAlreadyRequestedPurchaseException(detail: String = ""):
        InvalidRequestException(ErrorType.PRODUCT_ALREADY_REQUESTED_FOR_PURCHASE, detail)