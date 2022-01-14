package waffle.team6.carrot.purchaseOrders.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.NotAllowedException

class PurchaseOrderLookupByInvalidUserException(detail: String = ""):
        NotAllowedException(ErrorType.PURCHASE_ORDER_LOOKUP_NOT_ALLOWED, detail)