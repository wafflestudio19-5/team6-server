package waffle.team6.carrot.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.Category
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.model.ProductStatus
import waffle.team6.carrot.user.model.User

interface ProductRepository: JpaRepository<Product, Long?> {
    fun findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>, source: String
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>, source: String, title: String
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndPriceIsGreaterThanEqualAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>, source: String,
        title: String, minPrice: Long,
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndPriceIsLessThanEqualAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>, source: String,
        title: String, maxPrice: Long
    ): Page<Product>

    fun findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndPriceIsBetweenAndHiddenIsFalse(
        pageable: Pageable, categories: List<Category>, locations: List<String>, source: String,
        title: String, minPrice: Long, maxPrice: Long
    ): Page<Product>

    fun findAllByUserAndStatusInAndHiddenIsFalse(pageable: Pageable, user: User, status: List<ProductStatus>): Page<Product>

    fun findAllByUserAndHiddenIsTrue(pageable: Pageable, user: User): Page<Product>

    fun findAllByUserIdAndStatusIsInAndHiddenIsFalse(pageable: Pageable, userId: Long, status: List<ProductStatus>): Page<Product>
}