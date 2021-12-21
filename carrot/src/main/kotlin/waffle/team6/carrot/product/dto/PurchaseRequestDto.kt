package waffle.team6.carrot.product.dto

import jdk.jfr.BooleanFlag
import waffle.team6.carrot.product.model.PurchaseRequest
import waffle.team6.carrot.user.dto.UserDto
import java.time.LocalDateTime
import javax.validation.constraints.PositiveOrZero

class PurchaseRequestDto {
    data class Response(
        val user: UserDto.Response,
        val product: ProductDto.Response,
        val suggestedPrice: Long?,
        val accepted: Boolean,
        val updatedAt: LocalDateTime,
        val createdAt: LocalDateTime
    ) {
        constructor(purchaseRequest: PurchaseRequest): this(
            user = UserDto.Response(purchaseRequest.user),
            product = ProductDto.Response(purchaseRequest.product),
            suggestedPrice = purchaseRequest.suggestedPrice,
            accepted = purchaseRequest.accepted,
            updatedAt = purchaseRequest.updatedAt,
            createdAt = purchaseRequest.createdAt
        )
    }

    data class Request(
        @field:PositiveOrZero
        val suggestedPrice: Long?
    )
}