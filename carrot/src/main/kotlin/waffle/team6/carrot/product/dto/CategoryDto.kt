package waffle.team6.carrot.product.dto

import waffle.team6.carrot.product.model.Category

class CategoryDto {
    data class CategoryResponse(
        val categories: List<Category>
    )

    data class CategoryPutRequest(
        val categories: List<Int>
    )
}