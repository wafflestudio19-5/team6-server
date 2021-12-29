package waffle.team6.carrot.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.Category
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.model.Status

interface ProductRepository: JpaRepository<Product, Long?> {
    fun findAllByCategoryInAndLocationInAndStatusIs(
        pageable: Pageable, categories: List<Category>, locations: List<String>,
        status: Status
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndStatusIs(
        pageable: Pageable, categories: List<Category>, locations: List<String>,
        title: String, status: Status
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsGreaterThanEqualAndStatusIs(
        pageable: Pageable, categories: List<Category>, locations: List<String>,
        title: String, minPrice: Long, status: Status
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsLessThanEqualAndStatusIs(
        pageable: Pageable, categories: List<Category>, locations: List<String>,
        title: String, maxPrice: Long, status: Status
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsBetweenAndStatusIs(
        pageable: Pageable, categories: List<Category>, locations: List<String>,
        title: String, minPrice: Long, maxPrice: Long, status: Status
    ): Page<Product>

    fun findAllByUserId(
        pageable: Pageable, userId: Long
    ): Page<Product>
}