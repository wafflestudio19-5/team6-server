package waffle.team6.carrot.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.Product

interface ProductRepository: JpaRepository<Product, Long?> {
    fun findAllByTitleContaining(title: String): List<Product>
}