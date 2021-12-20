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
import java.time.LocalDateTime
@Service
class ProductService (
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
    private val purchaseRequestRepository: PurchaseRequestRepository,
    private val userRepository: UserRepository
){
    fun getProducts(): ListResponse<ProductDto.SimpleResponse> {
        return ListResponse(productRepository.findAll().map { ProductDto.SimpleResponse(it) })
    }

    fun getProductsByTitle(title: String): ListResponse<ProductDto.SimpleResponse> {
        return ListResponse(productRepository.findAllByTitleContaining(title).map { ProductDto.SimpleResponse(it) })
    }

    fun addProducts(user: User, productPostRequest: ProductDto.PostRequest): ProductDto.Response {
        val product = Product(user, productPostRequest)
        return ProductDto.Response(productRepository.save(product))
    }

    fun getProduct(id: Long): ProductDto.Response {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        product.hit += 1
        return ProductDto.Response(productRepository.save(product))
    }

    fun modifyProduct(user: User, productModifyRequest: ProductDto.ModifyRequest, id: Long): ProductDto.Response {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user != user) throw ProductModifyByInvalidUserException()
        return ProductDto.Response(productRepository.save(product.modify(productModifyRequest)))
    }

    fun deleteProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user != user) throw ProductDeleteByInvalidUserException()
        // val user = product.user
        // user.product.remove(product)
        // userRepository.save(user)
        productRepository.delete(product)
    }

    fun patchProduct(user: User, productPatchRequest: ProductDto.PatchRequest, id: Long): ProductDto.Response {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user != user) throw ProductModifyByInvalidUserException()

        if (productPatchRequest.images != null) product.images = productPatchRequest.images
        if (productPatchRequest.title != null) product.title = productPatchRequest.title
        if (productPatchRequest.content != null) product.content = productPatchRequest.content
        if (productPatchRequest.price != null) product.price = productPatchRequest.price
        if (productPatchRequest.negotiable != null) product.negotiable = productPatchRequest.negotiable
        if (productPatchRequest.category != null) product.category = productPatchRequest.category
        if (productPatchRequest.status != null) product.status = productPatchRequest.status

        product.updatedAt = LocalDateTime.now()
        return ProductDto.Response(productRepository.save(product))
    }

    fun likeProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()

//        if (!user.like.any { it.product == product}) {
//            val like = Like(user, product)
//            product.like += 1
//            user.like.add(like)
//            productRepository.save(product)
//            likeRepository.save(like)
//            userRepository.save(user)
//        }
    }

    fun unlikeProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()

//        val like = user.like.find { it.product == product }
//        if (like != null) {
//            product.like -= 1
//            user.like.remove(like)
//            productRepository.save(product)
//            likeRepository.delete(like)
//            userRepository.save(user)
//        }
    }

    fun chat(user: User, id: Long, request: PurchaseRequestDto.Request): PurchaseRequestDto.Response {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (product.purchaseRequest.any { it.user == user }) throw ProductAlreadyRequestedPurchaseException()
        val purchaseRequest = PurchaseRequest(user, product, request)
        if (request.suggestedPrice != null) product.priceSuggestion += 1
        product.chat += 1
        //user.purchaseRequest.add(request)
        product.purchaseRequest.add(purchaseRequest)
        productRepository.save(product)
        //userRepository.save(user)
        return PurchaseRequestDto.Response(purchaseRequestRepository.save(purchaseRequest))
    }

    fun reserve(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()
        if (product.user != user) throw ProductReserveByInvalidUserException()
        if (product.status == Status.FOR_SALE) {
            product.status = Status.RESERVED
            productRepository.save(product)
        }
    }

    fun getProductPurchaseRequests(user: User, id: Long): ListResponse<PurchaseRequestDto.Response> {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user != user) throw ProductPurchaseRequestLookupByInvalidUserException()
        return ListResponse(purchaseRequestRepository.findAllByProductId(id).map { PurchaseRequestDto.Response(it) })
    }
}

