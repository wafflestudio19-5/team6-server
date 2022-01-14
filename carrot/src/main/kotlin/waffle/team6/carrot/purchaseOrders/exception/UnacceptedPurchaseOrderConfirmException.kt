package waffle.team6.carrot.purchaseOrders.exception

import waffle.team6.global.common.exception.ErrorType
import waffle.team6.global.common.exception.InvalidRequestException

class UnacceptedPurchaseOrderConfirmException(detail: String = ""):
        InvalidRequestException(ErrorType.PURCHASE_ORDER_CONFIRMED_ONLY_WHEN_ACCEPTED, detail)