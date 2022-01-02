package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ConflictException
import waffle.team6.global.common.exception.ErrorType

class ProductAlreadyRequestedPurchaseException(detail: String = ""):
        ConflictException(ErrorType.PRODUCT_PURCHASE_REQUEST_CONFLICT, detail)