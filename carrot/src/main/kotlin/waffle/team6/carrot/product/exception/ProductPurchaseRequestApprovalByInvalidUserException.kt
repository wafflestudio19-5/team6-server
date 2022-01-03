package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class ProductPurchaseRequestApprovalByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PRODUCT_PURCHASE_REQUEST_APPROVAL_NOT_ALLOWED, detail)