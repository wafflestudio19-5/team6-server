package waffle.team6.carrot.purchaseOrders.dto

import jdk.jfr.BooleanFlag
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.purchaseOrders.model.PurchaseOrderStatus
import waffle.team6.carrot.purchaseOrders.model.PurchaseOrder
import waffle.team6.carrot.user.dto.UserDto
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

class PurchaseOrderDto {
    data class PurchaseOrderResponse(
        val id: Long,
        val user: UserDto.Response,
        val product: ProductDto.ProductResponse,
        val suggestedPrice: Long?,
        val message: String?,
        val lastMessageTime: LocalDateTime?,
        val status: PurchaseOrderStatus?,
        val updatedAt: LocalDateTime,
        val createdAt: LocalDateTime
    ) {
        constructor(purchaseOrder: PurchaseOrder, isSeller: Boolean): this(
            id = purchaseOrder.id,
            user = UserDto.Response(purchaseOrder.user),
            product = ProductDto.ProductResponse(purchaseOrder.product, isSeller),
            suggestedPrice = purchaseOrder.suggestedPrice,
            message = purchaseOrder.message,
            lastMessageTime = purchaseOrder.lastMessageTime,
            status = purchaseOrder.status,
            updatedAt = purchaseOrder.updatedAt,
            createdAt = purchaseOrder.createdAt
        )
    }

    data class PurchaseOrderResponseWithoutUser(
        val id: Long,
        val product: ProductDto.ProductSimpleResponse,
        val suggestedPrice: Long?,
        val status: PurchaseOrderStatus?,
        val updatedAt: LocalDateTime,
        val createdAt: LocalDateTime
    ) {
        constructor(purchaseOrder: PurchaseOrder): this(
            id = purchaseOrder.id,
            product = ProductDto.ProductSimpleResponse(purchaseOrder.product),
            suggestedPrice = purchaseOrder.suggestedPrice,
            status = purchaseOrder.status,
            updatedAt = purchaseOrder.updatedAt,
            createdAt = purchaseOrder.createdAt
        )
    }

    data class PurchaseOrderPostRequest(
        @field:Positive
        val productId: Long,
        @field:PositiveOrZero
        val suggestedPrice: Long?,
        val message: String? = null
    )

    data class PurchaseOrderUpdateRequest(
        @field:PositiveOrZero
        val suggestedPrice: Long?,
        val message: String? = null
    )

    data class PurchaseOrderHandleRequest(
        val action: String
    )
}