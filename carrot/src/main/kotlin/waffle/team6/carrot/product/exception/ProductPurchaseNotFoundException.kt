package waffle.team6.carrot.product.exception

import waffle.team6.global.common.exception.DataNotFoundException
import waffle.team6.global.common.exception.ErrorType

class ProductPurchaseNotFoundException(detail: String = ""):
        DataNotFoundException(ErrorType.PRODUCT_PURCHASE_REQUEST_NOT_FOUND, detail)