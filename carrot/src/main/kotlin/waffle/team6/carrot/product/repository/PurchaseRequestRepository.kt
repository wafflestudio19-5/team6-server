package waffle.team6.carrot.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.PurchaseRequest
import waffle.team6.carrot.user.model.User

interface PurchaseRequestRepository:JpaRepository<PurchaseRequest, Long?> {
    fun findAllByProductId(productId: Long): List<PurchaseRequest>

    fun findAllByProductIdAndSuggestedPriceIsNotNull(productId: Long): List<PurchaseRequest>

    fun findAllByUser(user: User): List<PurchaseRequest>
}