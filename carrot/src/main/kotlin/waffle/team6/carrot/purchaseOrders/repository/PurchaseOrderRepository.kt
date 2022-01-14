package waffle.team6.carrot.purchaseOrders.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.purchaseOrders.model.PurchaseOrder
import waffle.team6.carrot.purchaseOrders.model.PurchaseOrderStatus
import waffle.team6.carrot.user.model.User

interface PurchaseOrderRepository:JpaRepository<PurchaseOrder, Long?> {
    fun findAllByProductId(pageable: Pageable, productId: Long): Page<PurchaseOrder>

    fun findAllByProductIdAndSuggestedPriceIsNotNull(pageable: Pageable, productId: Long): Page<PurchaseOrder>

    fun findAllByProductIdAndStatusIs(pageable: Pageable, productId: Long, status: PurchaseOrderStatus): Page<PurchaseOrder>

    fun findAllByUserAndStatusIsIn(pageable: Pageable, user: User, status: List<PurchaseOrderStatus>): Page<PurchaseOrder>

    fun findAllByUserAndStatusIsNull(pageable: Pageable, user: User): Page<PurchaseOrder>
}