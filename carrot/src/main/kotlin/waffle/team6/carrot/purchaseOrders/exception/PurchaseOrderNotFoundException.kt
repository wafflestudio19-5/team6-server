package waffle.team6.carrot.purchaseOrders.exception

import waffle.team6.global.common.exception.DataNotFoundException
import waffle.team6.global.common.exception.ErrorType

class PurchaseOrderNotFoundException(detail: String = ""):
        DataNotFoundException(ErrorType.PRODUCT_PURCHASE_REQUEST_NOT_FOUND, detail)