package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class ProductPurchaseRequestRejectByInvalidUserException(detail: String = ""):
        InvalidRequestException(ErrorType.PRODUCT_PURCHASE_REQUEST_REJECT_NOT_ALLOWED, detail)