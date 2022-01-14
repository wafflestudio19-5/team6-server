package waffle.team6.carrot.purchaseOrders.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class PurchaseOrderUpdateByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PURCHASE_ORDER_UPDATE_NOT_ALLOWED, detail)