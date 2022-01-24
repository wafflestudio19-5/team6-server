package waffle.team6.carrot.product.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.image.model.Image
import waffle.team6.carrot.image.service.ImageService
import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.location.service.LocationService
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.exception.*
import waffle.team6.carrot.product.model.*
import waffle.team6.carrot.product.repository.LikeRepository
import waffle.team6.carrot.product.repository.ProductRepository
import waffle.team6.carrot.user.exception.UserLocationNotVerifiedException
import waffle.team6.carrot.user.exception.UserNotActiveException
import waffle.team6.carrot.user.model.User
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class ProductService (
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
    private val imageService: ImageService,
    private val locationService: LocationService,
){
    fun getProducts(user: User, pageNumber: Int, pageSize: Int): Page<ProductDto.ProductSimpleResponse> {
        val locations = locationService.findAdjacentLocationsByName(user.activeLocation, user.activeRangeOfLocation)
        return productRepository
            .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndHiddenIsFalse(
                PageRequest.of(pageNumber, pageSize, Sort.by("lastBringUpMyPost").descending()),
                user.categoriesOfInterest.map { it.category },
                locations,
                user.activeLocation
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
            user.activeLocation,
            searchRequest.rangeOfLocation ?: user.activeRangeOfLocation
        )
        val result: Page<Product>
        if (searchRequest.categories == null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndHiddenIsFalse(
                    pageRequest,
                    user.categoriesOfInterest.map { it.category },
                    locations,
                    user.activeLocation,
                    searchRequest.title
                )
        } else if (searchRequest.maxPrice != null && searchRequest.minPrice != null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndPriceIsBetweenAndHiddenIsFalse(
                    pageRequest,
                    searchRequest.categories,
                    locations,
                    user.activeLocation,
                    searchRequest.title,
                    searchRequest.minPrice,
                    searchRequest.maxPrice
                )
        } else if (searchRequest.minPrice != null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndPriceIsGreaterThanEqualAndHiddenIsFalse(
                    pageRequest,
                    searchRequest.categories,
                    locations,
                    user.activeLocation,
                    searchRequest.title,
                    searchRequest.minPrice
                )
        } else if (searchRequest.maxPrice != null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndPriceIsLessThanEqualAndHiddenIsFalse(
                    pageRequest,
                    searchRequest.categories,
                    locations,
                    user.activeLocation,
                    searchRequest.title,
                    searchRequest.maxPrice
                )
        } else {
            result = productRepository
                .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndHiddenIsFalse(
                    pageRequest,
                    searchRequest.categories,
                    locations,
                    user.activeLocation,
                    searchRequest.title
                )
        }
        return result.map { ProductDto.ProductSimpleResponse(it) }
    }

    @Transactional
    fun addProduct(user: User, productPostRequest: ProductDto.ProductPostRequest): ProductDto.ProductResponse {
        if (!user.activeLocationVerified) throw UserLocationNotVerifiedException()
        val images = productPostRequest.images?.map { imageService.getImageByIdAndCheckAuthorization(it, user) }
        val adjacentLocations = locationService
            .findAdjacentLocationsByName(user.activeLocation, RangeOfLocation.from(productPostRequest.rangeOfLocation))
        val product = productRepository.save(Product(user,
            images as MutableList<Image>?, adjacentLocations, productPostRequest))
        user.products.add(product)
        return ProductDto.ProductResponse(product, true)
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
        product.images?.forEach { imageService.delete(it.id, user) }
        user.products.remove(product)
        productRepository.delete(product)
    }

    @Transactional
    fun patchProduct(user: User, productPatchRequest: ProductDto.ProductUpdateRequest, id: Long
    ): ProductDto.ProductResponse {
        if (!user.activeLocationVerified) throw UserLocationNotVerifiedException()
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductModifyByInvalidUserException()
        if (product.status == ProductStatus.SOLD_OUT) throw ProductAlreadySoldOutException()
        val imagesToRemove = product.images?.filterNot { productPatchRequest.images?.contains(it.id) ?: true }
        val adjacentLocations = productPatchRequest.rangeOfLocation?.let { locationService
            .findAdjacentLocationsByName(product.location, RangeOfLocation.from(productPatchRequest.rangeOfLocation))}

        product.images = productPatchRequest.images
            ?.map { imageService.getImageByIdAndCheckAuthorization(it, user) }?.toMutableList() ?: product.images
        product.title = productPatchRequest.title ?: product.title
        product.content = productPatchRequest.content ?: product.content
        product.price = productPatchRequest.price ?: product.price
        product.negotiable = productPatchRequest.negotiable ?: product.negotiable
        product.category = productPatchRequest.category?.let { Category.from(it) } ?: product.category
        product.forAge = (if (productPatchRequest.category == 4) productPatchRequest.forAge
            ?.map { ForAge.from(it) } else null) as MutableList<ForAge>
        product.adjacentLocations = adjacentLocations ?: product.adjacentLocations
        product.rangeOfLocation = productPatchRequest.rangeOfLocation
            ?.let { RangeOfLocation.from(it) } ?: product.rangeOfLocation

        if (product.images?.isEmpty() == true) product.images= null
        imagesToRemove?.forEach { imageService.delete(it.id, user) }
        return ProductDto.ProductResponse(product, true)
    }

    @Transactional
    fun likeProduct(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id == user.id) throw ProductLikeBySellerException()
        if (!product.user.isActive) throw UserNotActiveException()
        if (product.status == ProductStatus.SOLD_OUT) throw ProductAlreadySoldOutException()

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
    fun changeProductStatusToReserved(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductStatusChangeByInvalidUserException()
        product.status = ProductStatus.RESERVED
    }

    @Transactional
    fun changeProductStatusToSoldOut(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductStatusChangeByInvalidUserException()
        product.status = ProductStatus.SOLD_OUT
    }

    @Transactional
    fun changeProductStatusToForSale(user: User, id: Long) {
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductStatusChangeByInvalidUserException()
        product.status = ProductStatus.FOR_SALE
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
        if (!user.activeLocationVerified) throw UserLocationNotVerifiedException()
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductBumpByInvalidUserException()
        if (product.lastBringUpMyPost.isBefore(LocalDateTime.now().minusDays(1))) {
            product.lastBringUpMyPost = LocalDateTime.now()
        } else throw ProductEarlyBumpException()
    }
}

