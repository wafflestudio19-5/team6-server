package waffle.team6.carrot.product.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
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
class ProductService (
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
    private val purchaseRequestRepository: PurchaseRequestRepository,
    private val userRepository: UserRepository,
//    private val imageRepository: ImageService
){
    fun getProducts(): ListResponse<ProductDto.SimpleResponse> {
        return ListResponse(productRepository.findAll().map { ProductDto.SimpleResponse(it) })
    }

    fun getProductsByTitle(title: String): ListResponse<ProductDto.SimpleResponse> {
        return ListResponse(productRepository.findAllByTitleContaining(title).map { ProductDto.SimpleResponse(it) })
    }

    fun addProducts(user: User, productPostRequest: ProductDto.PostRequest): ProductDto.Response {
        val product = Product(user, productPostRequest)
        return ProductDto.Response(productRepository.save(product), true)
    }

    fun getProduct(user: User, id: Long): ProductDto.Response {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        product.hit += 1
        return if (product.user.id == user.id) ProductDto.Response(product, true)
        else ProductDto.Response(product, false)
    }

    fun deleteProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductDeleteByInvalidUserException()
//        for (image in product.images) {
//            imageService.delete(image, user)
//        }
        productRepository.delete(product)
    }

    fun patchProduct(user: User, productPatchRequest: ProductDto.PatchRequest, id: Long): ProductDto.Response {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductModifyByInvalidUserException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()

        if (productPatchRequest.images != null) product.images = productPatchRequest.images
        if (productPatchRequest.title != null) product.title = productPatchRequest.title
        if (productPatchRequest.content != null) product.content = productPatchRequest.content
        if (productPatchRequest.price != null) product.price = productPatchRequest.price
        if (productPatchRequest.negotiable != null) product.negotiable = productPatchRequest.negotiable
        if (productPatchRequest.category != null) product.category = productPatchRequest.category

        productRepository.flush()
        return ProductDto.Response(product, true)
    }

    fun likeProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id == user.id) throw ProductLikeBySellerException()

//        if (!user.like.any { it.product == product}) {
//            val like = Like(user, product)
//            product.like += 1
//            user.like.add(like)
//        }
    }

    fun cancelLikeProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()

//        val like = user.like.find { it.product == product }
//        if (like != null) {
//            product.like -= 1
//            user.like.remove(like)
//            likeRepository.delete(like)
//        }
    }

    fun chat(user: User, id: Long, request: PurchaseRequestDto.Request): PurchaseRequestDto.Response {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (product.purchaseRequests.any { it.user == user }) throw ProductAlreadyRequestedPurchaseException()
        if (product.user.id == user.id) throw ProductChatBySellerException()
        val purchaseRequest = PurchaseRequest(user, product, request)
        if (request.suggestedPrice != null) product.priceSuggestions += 1
        product.chats += 1
        //user.purchaseRequest.add(request)
        product.purchaseRequests.add(purchaseRequest)
        //userRepository.flush()
        productRepository.flush()
        return PurchaseRequestDto.Response(purchaseRequestRepository.save(purchaseRequest), false)
    }

    fun reserve(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (product.user.id != user.id) throw ProductReserveByInvalidUserException()
        if (product.status == Status.FOR_SALE) product.status = Status.RESERVED
        productRepository.flush()
    }

    fun cancelReserve(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (product.user.id != user.id) throw ProductReserveCancelByInvalidUserException()
        if (product.status == Status.RESERVED) product.status = Status.FOR_SALE
        productRepository.flush()
    }

    fun getProductPurchaseRequests(user: User, id: Long): ListResponse<PurchaseRequestDto.Response> {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductPurchaseRequestLookupByInvalidUserException()
        return ListResponse(purchaseRequestRepository.findAllByProductId(id)
            .map { PurchaseRequestDto.Response(it, true) })
    }

    fun getProductPurchaseRequestsWithPriceSuggestion(user: User, id: Long): ListResponse<PurchaseRequestDto.Response> {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductPurchaseRequestLookupByInvalidUserException()
        return ListResponse(purchaseRequestRepository.findAllByProductIdAndSuggestedPriceIsNotNull(id)
            .map { PurchaseRequestDto.Response(it, true) })
    }

    fun getProductPurchaseRequest(user: User, productId: Long, id: Long): PurchaseRequestDto.Response {
        val purchaseRequest = purchaseRequestRepository.findByIdOrNull(id) ?: throw ProductPurchaseNotFoundException()
        if (purchaseRequest.product.id != productId) throw ProductPurchaseRequestMismatchException()
        if (purchaseRequest.product.user.id != user.id) throw ProductPurchaseRequestLookupByInvalidUserException()
        return PurchaseRequestDto.Response(purchaseRequest, true)
    }

    fun confirmProductPurchaseRequest(user: User, productId: Long, id: Long): PurchaseRequestDto.Response {
        productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        val purchaseRequest = purchaseRequestRepository.findByIdOrNull(id) ?: throw ProductPurchaseNotFoundException()
        if (purchaseRequest.product.id != productId) throw ProductPurchaseRequestMismatchException()
        if (purchaseRequest.product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (purchaseRequest.product.user.id != user.id) throw ProductPurchaseRequestConfirmByInvalidUserException()
        purchaseRequest.product.status = Status.SOLD_OUT
        purchaseRequest.accepted = true
        purchaseRequestRepository.flush()
        return PurchaseRequestDto.Response(purchaseRequest, true)
    }
}

