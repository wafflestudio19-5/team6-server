package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ProductPurchaseRequestLookupByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PRODUCT_PURCHASE_REQUEST_LOOKUP_NOT_ALLOWED, detail)