package waffle.team6.carrot.product.dto

import waffle.team6.carrot.user.dto.UserDto
import java.time.LocalDateTime
import javax.validation.constraints.PositiveOrZero

class PurchaseRequestDto {
    data class PurchaseRequestResponse(
        val user: UserDto.Response,
        val product: ProductDto.ProductResponse,
        val suggestedPrice: Long?,
        val message: String?,
        val accepted: Boolean?,
        val updatedAt: LocalDateTime,
        val createdAt: LocalDateTime
    ) {
        constructor(purchaseRequest: waffle.team6.carrot.product.model.PurchaseRequest, isSeller: Boolean): this(
            user = UserDto.Response(purchaseRequest.user),
            product = ProductDto.ProductResponse(purchaseRequest.product, isSeller),
            suggestedPrice = purchaseRequest.suggestedPrice,
            message = purchaseRequest.message,
            accepted = purchaseRequest.accepted,
            updatedAt = purchaseRequest.updatedAt,
            createdAt = purchaseRequest.createdAt
        )
    }

    data class PurchaseRequest(
        @field:PositiveOrZero
        val suggestedPrice: Long?,
        val message: String? = null
    )

    data class PurchaseRequestResponseWithoutUser(
        val product: ProductDto.ProductSimpleResponse,
        val suggestedPrice: Long?,
        val accepted: Boolean,
        val updatedAt: LocalDateTime,
        val createdAt: LocalDateTime
    ) {
        constructor(purchaseRequest: waffle.team6.carrot.product.model.PurchaseRequest): this(
            product = ProductDto.ProductSimpleResponse(purchaseRequest.product),
            suggestedPrice = purchaseRequest.suggestedPrice,
            accepted = purchaseRequest.accepted,
            updatedAt = purchaseRequest.updatedAt,
            createdAt = purchaseRequest.createdAt
        )
    }
}