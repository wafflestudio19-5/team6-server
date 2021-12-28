package waffle.team6.carrot.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.Category
import waffle.team6.carrot.product.model.Product

interface ProductRepository: JpaRepository<Product, Long?> {
    fun findAllByCategoryInAndLocationIn(
        pageable: Pageable, categories: List<Category>, locations: List<String>
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContaining(
        pageable: Pageable, categories: List<Category>, locations: List<String>, title: String
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsGreaterThanEqual(
        pageable: Pageable, categories: List<Category>, locations: List<String>, title: String, minPrice: Long
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsLessThanEqual(
        pageable: Pageable, categories: List<Category>, locations: List<String>, title: String, maxPrice: Long
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsBetween(
        pageable: Pageable, categories: List<Category>, locations: List<String>,
        title: String, minPrice: Long, maxPrice: Long
    ): Page<Product>
}