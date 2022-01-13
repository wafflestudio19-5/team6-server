package waffle.team6.carrot.purchaseOrders.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class PurchaseOrderDeleteByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PURCHASE_ORDER_DELETE_NOT_ALLOWED, detail)