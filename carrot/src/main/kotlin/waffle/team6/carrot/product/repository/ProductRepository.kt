package waffle.team6.carrot.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.Category
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.model.Status

interface ProductRepository: JpaRepository<Product, Long?> {
    fun findAllByCategoryInAndLocationInAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>, title: String
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsGreaterThanEqualAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>,
        title: String, minPrice: Long,
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsLessThanEqualAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>,
        title: String, maxPrice: Long
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsBetweenAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>,
        title: String, minPrice: Long, maxPrice: Long
    ): Page<Product>

    fun findAllByUserIdAndStatusInAndHiddenIsFalse(pageable: Pageable, userId: Long, status: List<Status>): Page<Product>

    fun findAllByUserIdAndHiddenIsTrue(pageable: Pageable, userId: Long): Page<Product>
}