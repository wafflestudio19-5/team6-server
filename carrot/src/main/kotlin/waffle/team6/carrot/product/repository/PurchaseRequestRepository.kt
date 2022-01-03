package waffle.team6.carrot.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.PurchaseRequest
import waffle.team6.carrot.user.model.User

interface PurchaseRequestRepository:JpaRepository<PurchaseRequest, Long?> {
    fun findAllByProductId(pageable: Pageable, productId: Long): Page<PurchaseRequest>

    fun findAllByUser(user: User): List<PurchaseRequest>

    fun findAllByProductIdAndSuggestedPriceIsNotNull(pageable: Pageable, productId: Long): Page<PurchaseRequest>

    fun findAllByUserIdAndAcceptedIsNull(pageable: Pageable, userId: Long): Page<PurchaseRequest>

    fun findAllByUserIdAndAcceptedIsTrue(pageable: Pageable, userId: Long): Page<PurchaseRequest>

    fun findAllByUserIdAndAcceptedIsFalse(pageable: Pageable, userId: Long): Page<PurchaseRequest>

}