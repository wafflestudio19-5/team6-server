package waffle.team6.carrot.product.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
        val location = if (user.isFirstLocationActive) user.firstLocation else user.secondLocation!!
        val rangeOfLocation = if (user.isFirstLocationActive) user.firstRangeOfLocation else user.secondRangeOfLocation!!
        val locations = locationService.findAdjacentLocationsByName(location, rangeOfLocation)
        return productRepository
            .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndHiddenIsFalse(
                PageRequest.of(pageNumber, pageSize, Sort.by("lastBringUpMyPost").descending()),
                user.categoriesOfInterest.map { it.category },
                locations,
                location
            )
            .map { ProductDto.ProductSimpleResponse(it) }
    }

    fun searchProducts(user: User, searchRequest: ProductDto.ProductSearchRequest
    ): Page<ProductDto.ProductSimpleResponse> {
        val location = if (user.isFirstLocationActive) user.firstLocation else user.secondLocation!!
        val rangeOfLocation = if (user.isFirstLocationActive) user.firstRangeOfLocation else user.secondRangeOfLocation!!
        val pageRequest = PageRequest.of(
            searchRequest.pageNumber,
            searchRequest.pageSize,
            Sort.by("lastBringUpMyPost").descending()
        )
        val locations = locationService.findAdjacentLocationsByName(location, searchRequest.rangeOfLocation ?: rangeOfLocation)
        val categories = searchRequest.categories ?: user.categoriesOfInterest.map { it.category }
        val result: Page<Product>
        if (searchRequest.maxPrice != null && searchRequest.minPrice != null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndPriceIsBetweenAndHiddenIsFalse(
                    pageRequest,
                    categories,
                    locations,
                    location,
                    searchRequest.title,
                    searchRequest.minPrice,
                    searchRequest.maxPrice
                )
        } else if (searchRequest.minPrice != null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndPriceIsGreaterThanEqualAndHiddenIsFalse(
                    pageRequest,
                    categories,
                    locations,
                    location,
                    searchRequest.title,
                    searchRequest.minPrice
                )
        } else if (searchRequest.maxPrice != null) {
            result = productRepository
                .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndPriceIsLessThanEqualAndHiddenIsFalse(
                    pageRequest,
                    categories,
                    locations,
                    location,
                    searchRequest.title,
                    searchRequest.maxPrice
                )
        } else {
            result = productRepository
                .findAllByCategoryInAndLocationInAndAdjacentLocationsEqualsAndTitleContainingAndHiddenIsFalse(
                    pageRequest,
                    categories,
                    locations,
                    location,
                    searchRequest.title
                )
        }
        return result.map { ProductDto.ProductSimpleResponse(it) }
    }

    @Transactional
    fun addProduct(user: User, productPostRequest: ProductDto.ProductPostRequest): ProductDto.ProductResponse {
        if ((user.isFirstLocationActive && !user.firstLocationVerified)||
            (!user.isFirstLocationActive && !user.secondLocationVerified)) throw UserLocationNotVerifiedException()
        val location = if (user.isFirstLocationActive) user.firstLocation else user.secondLocation!!
        val adjacentLocations = locationService
            .findAdjacentLocationsByName(location, RangeOfLocation.from(productPostRequest.rangeOfLocation))
        val product = productRepository.save(Product(user, adjacentLocations, productPostRequest))
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
        product.imageUrls.forEach { imageService.deleteByUrl(it, user.id) }
        user.products.remove(product)
        productRepository.delete(product)
    }

    @Transactional
    fun patchProduct(user: User, productPatchRequest: ProductDto.ProductUpdateRequest, id: Long
    ): ProductDto.ProductResponse {
        if ((user.isFirstLocationActive && !user.firstLocationVerified)||
            (!user.isFirstLocationActive && !user.secondLocationVerified)) throw UserLocationNotVerifiedException()
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductModifyByInvalidUserException()
        if (product.status == ProductStatus.SOLD_OUT) throw ProductAlreadySoldOutException()
        val imagesToRemove = product.imageUrls.filterNot { productPatchRequest.imageUrls?.contains(it) ?: true }
        val adjacentLocations = productPatchRequest.rangeOfLocation?.let { locationService
            .findAdjacentLocationsByName(product.location, RangeOfLocation.from(productPatchRequest.rangeOfLocation))}
        product.modify(productPatchRequest, adjacentLocations)
        if (product.imageUrls.isEmpty()) product.imageUrls= listOf()
        imagesToRemove.forEach { imageService.deleteByUrl(it, user.id) }
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
        if ((user.isFirstLocationActive && !user.firstLocationVerified)||
            (!user.isFirstLocationActive && !user.secondLocationVerified)) throw UserLocationNotVerifiedException()
        val product = productRepository.findByIdOrNull(id) ?: throw ProductNotFoundException()
        if (product.user.id != user.id) throw ProductBumpByInvalidUserException()
        if (product.lastBringUpMyPost.isBefore(LocalDateTime.now().minusDays(1))) {
            product.lastBringUpMyPost = LocalDateTime.now()
        } else throw ProductEarlyBumpException()
    }
}

