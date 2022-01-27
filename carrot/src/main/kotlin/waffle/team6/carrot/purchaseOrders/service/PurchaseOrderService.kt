package waffle.team6.carrot.purchaseOrders.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.product.exception.*
import waffle.team6.carrot.product.model.ProductStatus
import waffle.team6.carrot.product.repository.ProductRepository
import waffle.team6.carrot.purchaseOrders.dto.PurchaseOrderDto
import waffle.team6.carrot.purchaseOrders.exception.*
import waffle.team6.carrot.purchaseOrders.model.PurchaseOrder
import waffle.team6.carrot.purchaseOrders.model.PurchaseOrderStatus
import waffle.team6.carrot.purchaseOrders.repository.PurchaseOrderRepository
import waffle.team6.carrot.user.exception.UserLocationNotVerifiedException
import waffle.team6.carrot.user.exception.UserNotActiveException
import waffle.team6.carrot.user.model.User

@Service
@Transactional(readOnly = true)
class PurchaseOrderService(
    private val productRepository: ProductRepository,
    private val purchaseOrderRepository: PurchaseOrderRepository
) {
    fun getProductPurchaseRequests(user: User, productId: Long, pageNumber: Int, pageSize: Int
    ): Page<PurchaseOrderDto.PurchaseOrderResponse> {
        val product = productRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw PurchaseOrderLookupByInvalidUserException()
        return purchaseOrderRepository.findAllByProductId(
            PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending()),
            productId
        ).map { PurchaseOrderDto.PurchaseOrderResponse(it, true) }
    }

    fun getProductPurchaseRequestsWithPriceSuggestion(user: User, productId: Long, pageNumber: Int, pageSize: Int
    ): Page<PurchaseOrderDto.PurchaseOrderResponse> {
        val product = productRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw PurchaseOrderLookupByInvalidUserException()
        return purchaseOrderRepository.findAllByProductIdAndSuggestedPriceIsNotNull(
            PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending()),
            productId
        ).map { PurchaseOrderDto.PurchaseOrderResponse(it, true) }
    }

    fun getAcceptedProductPurchaseRequests(user: User, productId: Long, pageNumber: Int, pageSize: Int
    ): Page<PurchaseOrderDto.PurchaseOrderResponse> {
        val product = productRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw PurchaseOrderLookupByInvalidUserException()
        return purchaseOrderRepository.findAllByProductIdAndStatusIs(
            PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending()),
            productId,
            PurchaseOrderStatus.ACCEPTED
        ).map { PurchaseOrderDto.PurchaseOrderResponse(it, true) }
    }

    fun getProductPurchaseRequest(user: User, id: Long): PurchaseOrderDto.PurchaseOrderResponse {
        val purchaseOrder = purchaseOrderRepository.findByIdOrNull(id) ?: throw PurchaseOrderNotFoundException()
        if (purchaseOrder.product.user.id != user.id) throw PurchaseOrderLookupByInvalidUserException()
        return PurchaseOrderDto.PurchaseOrderResponse(purchaseOrder, true)
    }

    @Transactional
    fun chat(user: User, request: PurchaseOrderDto.PurchaseOrderPostRequest
    ): PurchaseOrderDto.PurchaseOrderResponse {
        if (!user.firstLocationVerified) throw UserLocationNotVerifiedException()
        val product = productRepository.findByIdOrNull(request.productId) ?: throw ProductNotFoundException()
        if (product.status == ProductStatus.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (product.purchaseOrders.any { it.user.id == user.id }) throw ProductAlreadyRequestedPurchaseException()
        if (!product.user.isActive) throw UserNotActiveException()
        if (product.user.id == user.id) throw ProductChatBySellerException()
        val purchaseOrder = PurchaseOrder(user, product, request)
        if (request.suggestedPrice != null) product.priceSuggestions += 1
        product.chats += 1
        user.purchaseOrders.add(purchaseOrder)
        product.purchaseOrders.add(purchaseOrder)
        return PurchaseOrderDto.PurchaseOrderResponse(purchaseOrderRepository.save(purchaseOrder), false)
    }

    @Transactional
    fun chatAgain(user: User, id: Long, request: PurchaseOrderDto.PurchaseOrderUpdateRequest
    ): PurchaseOrderDto.PurchaseOrderResponse {
        if (!user.firstLocationVerified) throw UserLocationNotVerifiedException()
        val purchaseOrder = purchaseOrderRepository.findByIdOrNull(id) ?: throw PurchaseOrderNotFoundException()
        if (purchaseOrder.product.status == ProductStatus.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (purchaseOrder.status == PurchaseOrderStatus.ACCEPTED ||
            purchaseOrder.status == PurchaseOrderStatus.CONFIRMED) throw PurchaseOrderAlreadyHandledException()
        if (purchaseOrder.product.user.isActive) throw UserNotActiveException()
        if (purchaseOrder.user.id != user.id) throw PurchaseOrderUpdateByInvalidUserException()
        return PurchaseOrderDto.PurchaseOrderResponse(purchaseOrder.update(request), false)
    }

    @Transactional
    fun deleteChat(user: User, id: Long) {
        val purchaseOrder = purchaseOrderRepository.findByIdOrNull(id) ?: throw PurchaseOrderNotFoundException()
        if (purchaseOrder.status == PurchaseOrderStatus.ACCEPTED ||
            purchaseOrder.status == PurchaseOrderStatus.CONFIRMED) throw PurchaseOrderAlreadyHandledException()
        if (purchaseOrder.user.id != user.id) throw PurchaseOrderDeleteByInvalidUserException()
        user.purchaseOrders.remove(purchaseOrder)
        purchaseOrderRepository.delete(purchaseOrder)
    }

    @Transactional
    fun handleProductOrder(user: User, id: Long, request: PurchaseOrderDto.PurchaseOrderHandleRequest
    ): PurchaseOrderDto.PurchaseOrderResponse {
        val purchaseOrder = purchaseOrderRepository.findByIdOrNull(id) ?: throw PurchaseOrderNotFoundException()
        if (purchaseOrder.product.status == ProductStatus.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (purchaseOrder.product.user.id != user.id) throw PurchaseOrderHandledByInvalidUserException()
        when (request.action) {
            "accept" -> {
                if (purchaseOrder.status != null) throw PurchaseOrderAlreadyHandledException()
                purchaseOrder.status = PurchaseOrderStatus.ACCEPTED
            }
            "reject" -> {
                if (purchaseOrder.status != null) throw PurchaseOrderAlreadyHandledException()
                purchaseOrder.status = PurchaseOrderStatus.REJECTED
            }
            "confirm" -> {
                if (purchaseOrder.status != PurchaseOrderStatus.ACCEPTED) throw UnacceptedPurchaseOrderConfirmException()
                purchaseOrder.status = PurchaseOrderStatus.CONFIRMED
                purchaseOrder.product.status = ProductStatus.SOLD_OUT
                for (otherPurchaseOrder in purchaseOrder.product.purchaseOrders) {
                    if (otherPurchaseOrder.id != purchaseOrder.id)
                        otherPurchaseOrder.status = PurchaseOrderStatus.REJECTED
                }
            }
        }
        return PurchaseOrderDto.PurchaseOrderResponse(purchaseOrder, true)
    }
}