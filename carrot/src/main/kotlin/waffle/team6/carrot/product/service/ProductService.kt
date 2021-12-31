package waffle.team6.carrot.product.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.image.model.Image
import waffle.team6.carrot.image.service.ImageService
import waffle.team6.carrot.location.service.LocationService
import waffle.team6.carrot.product.dto.ListResponse
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.dto.PurchaseRequestDto
import waffle.team6.carrot.product.exception.*
import waffle.team6.carrot.product.model.*
import waffle.team6.carrot.product.repository.LikeRepository
import waffle.team6.carrot.product.repository.ProductRepository
import waffle.team6.carrot.product.repository.PurchaseRequestRepository
import waffle.team6.carrot.user.model.User
import java.sql.SQLIntegrityConstraintViolationException
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class ProductService (
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
    private val purchaseRequestRepository: PurchaseRequestRepository,
    private val imageService: ImageService,
    private val locationService: LocationService,
){
    fun getProducts(user: User, pageNumber: Int, pageSize: Int): Page<ProductDto.ProductSimpleResponse> {
        val locations = locationService.findAdjacentLocationsByName(user.location, user.rangeOfLocation)
        return productRepository
            .findAllByCategoryInAndLocationInAndHiddenIsFalse(
                PageRequest.of(pageNumber, pageSize, Sort.by("lastBringUpMyPost").descending()),
                user.categoriesOfInterest.map { it.category },
                locations
            )
            .map { ProductDto.ProductSimpleResponse(it) }
    }

    fun searchProducts(user: User, searchRequest: ProductDto.ProductSearchRequest
    ): Page<ProductDto.ProductSimpleResponse> {
        val pageRequest = PageRequest.of(
            searchRequest.pageNumber,
            searchRequest.pageSize,
            Sort.by("lastBringUpMyPost").descending()
        )
        val locations = locationService.findAdjacentLocationsByName(
            user.location,
            searchRequest.rangeOfLocation ?: user.rangeOfLocation
        )
        val result: Page<Product>
        if (searchRequest.categories == null) {
            result = productRepository.findAllByCategoryInAndLocationInAndTitleContainingAndHiddenIsFalse(
                pageRequest,
                user.categoriesOfInterest.map { it.category },
                locations,
                searchRequest.title
            )
        } else if (searchRequest.maxPrice != null && searchRequest.minPrice != null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsBetweenAndHiddenIsFalse(
                    pageRequest,
                    searchRequest.categories,
                    locations,
                    searchRequest.title,
                    searchRequest.minPrice,
                    searchRequest.maxPrice
                )
        } else if (searchRequest.minPrice != null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsGreaterThanEqualAndHiddenIsFalse(
                    pageRequest,
                    searchRequest.categories,
                    locations,
                    searchRequest.title,
                    searchRequest.minPrice
                )
        } else if (searchRequest.maxPrice != null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndTitleContainingAndPriceIsLessThanEqualAndHiddenIsFalse(
                    pageRequest,
                    searchRequest.categories,
                    locations,
                    searchRequest.title,
                    searchRequest.maxPrice
                )
        } else {
            result = productRepository.findAllByCategoryInAndLocationInAndTitleContainingAndHiddenIsFalse(
                pageRequest,
                searchRequest.categories,
                locations,
                searchRequest.title
            )
        }
        return result.map { ProductDto.ProductSimpleResponse(it) }
    }

    @Transactional
    fun addProduct(user: User, productPostRequest: ProductDto.ProductPostRequest): ProductDto.ProductResponse {
        val images: MutableList<Image> = mutableListOf()
        for (imageId in productPostRequest.images) {
            images.add(imageService.getImageByIdAndCheckAuthorization(imageId, user))
        }
        try {
            val product = productRepository.save(Product(user, images, productPostRequest))
            user.products.add(product)
            return ProductDto.ProductResponse(product, true)
        } catch (e: SQLIntegrityConstraintViolationException) {
            throw ProductImageDuplicateException()
        }
    }

    @Transactional
    fun getProduct(user: User, id: Long): ProductDto.ProductResponse {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        return if (product.user.id == user.id) ProductDto.ProductResponse(product, true)
        else {
            if (product.hidden) throw HiddenProductAccessException()
            product.hit += 1
            ProductDto.ProductResponse(product, false)
        }
    }

    @Transactional
    fun deleteProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductDeleteByInvalidUserException()
        for (image in product.images) {
            imageService.delete(image.id, user)
        }
        user.products.remove(product)
        productRepository.delete(product)
    }

    @Transactional
    fun patchProduct(user: User, productPatchRequest: ProductDto.ProductUpdateRequest, id: Long
    ): ProductDto.ProductResponse {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductModifyByInvalidUserException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()

        product.images = productPatchRequest.images
            ?.map { imageService.getImageByIdAndCheckAuthorization(it, user) }?.toMutableList() ?: product.images
        product.title = productPatchRequest.title ?: product.title
        product.content = productPatchRequest.content ?: product.content
        product.price = productPatchRequest.price ?: product.price
        product.negotiable = productPatchRequest.negotiable ?: product.negotiable
        product.category = productPatchRequest.category?.let { Category.from(it) } ?: product.category

        return ProductDto.ProductResponse(product, true)
    }

    @Transactional
    fun likeProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id == user.id) throw ProductLikeBySellerException()
        if (product.status == Status.SOLD_OUT) throw ProductAlreadySoldOutException()

        if (!user.likes.any { it.product.id == product.id}) {
            val like = likeRepository.save(Like(user, product))
            product.likes += 1
            user.likes.add(like)
        }
    }

    @Transactional
    fun cancelLikeProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()

        val like = user.likes.find { it.product.id == product.id }
        if (like != null) {
            product.likes -= 1
            user.likes.remove(like)
            likeRepository.delete(like)
        }
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
        user.purchaseRequests.add(purchaseRequest)
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

    @Transactional
    fun hideProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductHideByInvalidUserException()
        product.hidden = true
    }

    @Transactional
    fun showProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductShowByInvalidUserException()
        product.hidden = false
    }

    @Transactional
    fun bringUpMyPost(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductBumpByInvalidUserException()
        if (product.lastBringUpMyPost.isBefore(LocalDateTime.now().minusDays(1))) {
            product.lastBringUpMyPost = LocalDateTime.now()
        } else throw ProductEarlyBumpException()
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

