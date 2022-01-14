package waffle.team6.carrot.purchaseOrders.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class PurchaseOrderHandledByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PURCHASE_ORDER_HANDLE_NOT_ALLOWED, detail)