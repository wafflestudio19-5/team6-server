package waffle.team6.carrot.product.dto

import waffle.team6.carrot.product.model.Like

class LikeDto {
    data class LikeResponse(
        val productDto: ProductDto.ProductSimpleResponse
    ) {
        constructor(like: Like): this(
            productDto = ProductDto.ProductSimpleResponse(like.product)
        )
    }
}