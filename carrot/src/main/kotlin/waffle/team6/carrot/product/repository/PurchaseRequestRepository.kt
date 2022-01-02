package waffle.team6.carrot.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.PurchaseRequest

interface PurchaseRequestRepository:JpaRepository<PurchaseRequest, Long?> {
    fun findAllByProductId(pageable: Pageable, productId: Long): Page<PurchaseRequest>

    fun findAllByProductIdAndSuggestedPriceIsNotNull(pageable: Pageable, productId: Long): Page<PurchaseRequest>
}