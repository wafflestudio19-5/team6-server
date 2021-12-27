package waffle.team6.carrot.product.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.product.dto.ListResponse
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.dto.PurchaseRequestDto
import waffle.team6.carrot.product.exception.*
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.model.PurchaseRequest
import waffle.team6.carrot.product.model.Status
import waffle.team6.carrot.product.repository.LikeRepository
import waffle.team6.carrot.product.repository.ProductRepository
import waffle.team6.carrot.product.repository.PurchaseRequestRepository
import waffle.team6.carrot.user.repository.UserRepository
import waffle.team6.carrot.user.model.User

@Service
@Transactional(readOnly = true)
class ProductService (
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
    private val purchaseRequestRepository: PurchaseRequestRepository,
    private val userRepository: UserRepository,
//    private val imageRepository: ImageService
){
    fun getProducts(): ListResponse<ProductDto.ProductSimpleResponse> {
        return ListResponse(productRepository.findAll().map { ProductDto.ProductSimpleResponse(it) })
    }

    fun getProductsByTitle(title: String): ListResponse<ProductDto.ProductSimpleResponse> {
        return ListResponse(productRepository
            .findAllByTitleContaining(title).map { ProductDto.ProductSimpleResponse(it) })
    }

    @Transactional
    fun addProducts(user: User, productPostRequest: ProductDto.ProductPostRequest): ProductDto.ProductResponse {
        val product = Product(user, productPostRequest)
//        user.products.add(product)
        return ProductDto.ProductResponse(productRepository.save(product), true)
    }

    @Transactional
    fun getProduct(user: User, id: Long): ProductDto.ProductResponse {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        product.hit += 1
        return if (product.user.id == user.id) ProductDto.ProductResponse(product, true)
        else ProductDto.ProductResponse(product, false)
    }

    @Transactional
    fun deleteProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductDeleteByInvalidUserException()
//        for (image in product.images) {
//            imageService.delete(image, user)
//        }
//        user.products.remove(product)
        productRepository.delete(product)
    }

    @Transactional
    fun patchProduct(user: User, productPatchRequest: ProductDto.ProductUpdateRequest, id: Long
    ): ProductDto.ProductResponse {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductModifyByInvalidUserException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()

        product.images = productPatchRequest.images ?: product.images
        product.title = productPatchRequest.title ?: product.title
        product.content = productPatchRequest.content ?: product.content
        product.price = productPatchRequest.price ?: product.price
        product.negotiable = productPatchRequest.negotiable ?: product.negotiable
        product.category = productPatchRequest.category ?: product.category

        return ProductDto.ProductResponse(product, true)
    }

    @Transactional
    fun likeProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id == user.id) throw ProductLikeBySellerException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()

//        if (!user.like.any { it.product == product}) {
//            val like = Like(user, product)
//            product.like += 1
//            user.like.add(like)
//        }
    }

    @Transactional
    fun cancelLikeProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()

//        val like = user.like.find { it.product == product }
//        if (like != null) {
//            product.like -= 1
//            user.like.remove(like)
//            likeRepository.delete(like)
//        }
    }

    @Transactional
    fun chat(user: User, id: Long, request: PurchaseRequestDto.PurchaseRequest
    ): PurchaseRequestDto.PurchaseRequestResponse {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (product.purchaseRequests.any { it.user.id == user.id }) throw ProductAlreadyRequestedPurchaseException()
        if (product.user.id == user.id) throw ProductChatBySellerException()
        val purchaseRequest = PurchaseRequest(user, product, request)
        if (request.suggestedPrice != null) product.priceSuggestions += 1
        product.chats += 1
        //user.purchaseRequest.add(request)
        product.purchaseRequests.add(purchaseRequest)
        return PurchaseRequestDto.PurchaseRequestResponse(purchaseRequestRepository.save(purchaseRequest), false)
    }

    @Transactional
    fun reserve(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (product.user.id != user.id) throw ProductReserveByInvalidUserException()
        if (product.status == Status.FOR_SALE) product.status = Status.RESERVED
    }

    @Transactional
    fun cancelReserve(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (product.user.id != user.id) throw ProductReserveCancelByInvalidUserException()
        if (product.status == Status.RESERVED) product.status = Status.FOR_SALE
    }

    fun getProductPurchaseRequests(user: User, id: Long): ListResponse<PurchaseRequestDto.PurchaseRequestResponse> {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductPurchaseRequestLookupByInvalidUserException()
        return ListResponse(purchaseRequestRepository.findAllByProductId(id)
            .map { PurchaseRequestDto.PurchaseRequestResponse(it, true) })
    }

    fun getProductPurchaseRequestsWithPriceSuggestion(user: User, id: Long
    ): ListResponse<PurchaseRequestDto.PurchaseRequestResponse> {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductPurchaseRequestLookupByInvalidUserException()
        return ListResponse(purchaseRequestRepository.findAllByProductIdAndSuggestedPriceIsNotNull(id)
            .map { PurchaseRequestDto.PurchaseRequestResponse(it, true) })
    }

    fun getProductPurchaseRequest(user: User, productId: Long, id: Long): PurchaseRequestDto.PurchaseRequestResponse {
        val purchaseRequest = purchaseRequestRepository.findByIdOrNull(id) ?: throw ProductPurchaseNotFoundException()
        if (purchaseRequest.product.id != productId) throw ProductPurchaseRequestMismatchException()
        if (purchaseRequest.product.user.id != user.id) throw ProductPurchaseRequestLookupByInvalidUserException()
        return PurchaseRequestDto.PurchaseRequestResponse(purchaseRequest, true)
    }

    @Transactional
    fun confirmProductPurchaseRequest(user: User, productId: Long, id: Long
    ): PurchaseRequestDto.PurchaseRequestResponse {
        productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        val purchaseRequest = purchaseRequestRepository.findByIdOrNull(id) ?: throw ProductPurchaseNotFoundException()
        if (purchaseRequest.product.id != productId) throw ProductPurchaseRequestMismatchException()
        if (purchaseRequest.product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (purchaseRequest.product.user.id != user.id) throw ProductPurchaseRequestConfirmByInvalidUserException()
        purchaseRequest.product.status = Status.SOLD_OUT
        purchaseRequest.accepted = true
        return PurchaseRequestDto.PurchaseRequestResponse(purchaseRequest, true)
    }
}

