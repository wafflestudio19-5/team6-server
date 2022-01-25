package waffle.team6.carrot.user.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.image.service.ImageService
import waffle.team6.carrot.product.dto.LikeDto
import waffle.team6.carrot.product.dto.PhraseDto
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.purchaseOrders.dto.PurchaseOrderDto
import waffle.team6.carrot.product.model.CategoryOfInterest
import waffle.team6.carrot.product.model.ProductStatus
import waffle.team6.carrot.product.repository.CategoryOfInterestRepository
import waffle.team6.carrot.product.repository.LikeRepository
import waffle.team6.carrot.product.repository.ProductRepository
import waffle.team6.carrot.purchaseOrders.model.PurchaseOrderStatus
import waffle.team6.carrot.purchaseOrders.repository.PurchaseOrderRepository
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.exception.InvalidStatusException
import waffle.team6.carrot.user.exception.UserAlreadyExistException
import waffle.team6.carrot.user.exception.UserInvalidCurrentPasswordException
import waffle.team6.carrot.user.exception.UserNotFoundException
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.repository.UserRepository

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val purchaseOrderRepository: PurchaseOrderRepository,
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
    private val categoryOfInterestRepository: CategoryOfInterestRepository,
    private val imageService: ImageService
) {
    @Transactional
    fun createUser(signUpRequest: UserDto.SignUpRequest): UserDto.Response {
        if (userRepository.findByName(signUpRequest.name) != null) throw UserAlreadyExistException()
        return UserDto.Response(userRepository.save(User(signUpRequest, passwordEncoder.encode(signUpRequest.password))))
    }

    @Transactional
    fun updateUserProfile(user: User, updateProfileRequest: UserDto.UpdateProfileRequest): UserDto.Response {
        if (updateProfileRequest.imageUrl != null) user.imageUrl?.let { imageService.deleteByUrl(it, user.id) }
        return UserDto.Response(user.modifyProfile(updateProfileRequest))
    }

    @Transactional
    fun deleteUserImage(user: User): UserDto.Response {
        user.imageUrl?.let { imageService.deleteByUrl(it, user.id) }
        return UserDto.Response(user.deleteImage())
    }

    @Transactional
    fun updateUserPassword(user: User, updatePasswordRequest: UserDto.UpdatePasswordRequest): UserDto.Response {
        if (!passwordEncoder.matches(updatePasswordRequest.currentPassword, user.password)) {
            throw UserInvalidCurrentPasswordException("Incorrect Current Password")
        }
        user.password = passwordEncoder.encode(updatePasswordRequest.newPassword)
        return UserDto.Response(userRepository.save(user))
    }

    @Transactional
    fun verifyUserLocation(user: User): UserDto.Response {
        return UserDto.Response(user.verifyLocation())
    }

    @Transactional
    fun addAnotherUserLocation(user: User, updateLocationRequest: UserDto.UpdateLocationRequest): UserDto.Response {
        return UserDto.Response(user.addLocation(updateLocationRequest))
    }

    @Transactional
    fun updateUserCurrentLocation(user: User, updateLocationRequest: UserDto.UpdateLocationRequest): UserDto.Response {
        return UserDto.Response(user.updateLocation(updateLocationRequest))
    }

    @Transactional
    fun deleteUserInactiveLocation(user: User): UserDto.Response {
        return UserDto.Response(user.deleteLocation())
    }

    @Transactional
    fun changeToAnotherUserLocation(user: User): UserDto.Response {
        return UserDto.Response(user.changeLocation())
    }

    @Transactional
    fun deleteMyAccount(user: User) {
        user.isActive = false
        for (purchaseOrder in user.purchaseOrders) {
            if (purchaseOrder.status == null || purchaseOrder.status == PurchaseOrderStatus.REJECTED) {
                purchaseOrderRepository.delete(purchaseOrder)
                user.purchaseOrders.remove(purchaseOrder)
            }
        }
    }

    fun isUserNameDuplicated(name: String): Boolean {
        return userRepository.existsByName(name)
    }

    fun findMe(user: User): UserDto.Response {
        return UserDto.Response(user)
    }

    fun findWithId(id: Long): UserDto.Response {
        return UserDto.Response(userRepository.findByIdOrNull(id) ?: throw UserNotFoundException())
    }

    @Transactional
    fun addMyPhrase(user: User, phrase: PhraseDto.PhrasePostRequest): PhraseDto.PhraseResponse {
        user.myPhrases.add(phrase.phrase)
        return PhraseDto.PhraseResponse(user.myPhrases)
    }

    @Transactional
    fun deleteMyPhrase(user: User, index: Int) {
        user.myPhrases.removeAt(index)
    }

    fun getMyPhrases(user: User): PhraseDto.PhraseResponse {
        return PhraseDto.PhraseResponse(user.myPhrases)
    }

    fun findMyPurchaseRequests(user: User, pageNumber: Int, pageSize: Int, status: String
    ): Page<PurchaseOrderDto.PurchaseOrderResponseWithoutUser> {
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending())
        return when (status) {
            "pending" -> purchaseOrderRepository.findAllByUserAndStatusIsNull(pageRequest, user)
            "accepted" -> purchaseOrderRepository.findAllByUserAndStatusIsIn(
                pageRequest,
                user,
                listOf(PurchaseOrderStatus.ACCEPTED, PurchaseOrderStatus.CONFIRMED)
            )
            "rejected" -> purchaseOrderRepository.findAllByUserAndStatusIsIn(
                pageRequest,
                user,
                listOf(PurchaseOrderStatus.REJECTED)
            )
            else -> throw InvalidStatusException()
        }.map { PurchaseOrderDto.PurchaseOrderResponseWithoutUser(it) }
    }

    fun findMyProducts(user: User, pageNumber: Int, pageSize: Int, status: String
    ): Page<ProductDto.ProductSimpleResponseWithoutUser> {
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("lastBringUpMyPost").descending())
        return when (status) {
            "for-sale" -> productRepository.findAllByUserAndStatusInAndHiddenIsFalse(
                pageRequest,
                user,
                listOf(ProductStatus.FOR_SALE, ProductStatus.RESERVED)
            )
            "sold-out" -> productRepository.findAllByUserAndStatusInAndHiddenIsFalse(
                pageRequest,
                user,
                listOf(ProductStatus.SOLD_OUT)
            )
            "hidden" -> productRepository.findAllByUserAndHiddenIsTrue(pageRequest, user)
            else -> throw InvalidStatusException()
        }.map { ProductDto.ProductSimpleResponseWithoutUser(it) }
    }

    fun findMyCategoriesOfInterests(user: User): List<CategoryOfInterest> {
        return categoryOfInterestRepository.findAllByUser(user)
    }

    fun findMyLikes(user: User, pageNumber: Int, pageSize: Int): Page<LikeDto.LikeResponse> {
        return likeRepository.findAllByUserId(
            PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()), user.id).map {
            LikeDto.LikeResponse(it)
        }
    }

    fun findUser(pageNumber: Int, pageSize: Int, name: String): Page<UserDto.UserSimpleResponse> {
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending())
        return userRepository.findAllByNameContainingOrNicknameContaining(pageRequest, name, name)
            .map { UserDto.UserSimpleResponse(it) }
    }

    fun findUserProducts(userId: Long, pageNumber: Int, pageSize: Int, status: String
    ): Page<ProductDto.ProductSimpleResponseWithoutUser> {
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("lastBringUpMyPost").descending())
        return when (status) {
            "all" -> productRepository.findAllByUserIdAndStatusIsInAndHiddenIsFalse(
                pageRequest,
                userId,
                listOf(ProductStatus.FOR_SALE, ProductStatus.RESERVED, ProductStatus.SOLD_OUT)
            )
            "for-sale" -> productRepository.findAllByUserIdAndStatusIsInAndHiddenIsFalse(
                pageRequest,
                userId,
                listOf(ProductStatus.FOR_SALE, ProductStatus.RESERVED)
            )
            "sold-out" -> productRepository.findAllByUserIdAndStatusIsInAndHiddenIsFalse(
                pageRequest,
                userId,
                listOf(ProductStatus.SOLD_OUT)
            )
            else -> throw InvalidStatusException()
        }.map { ProductDto.ProductSimpleResponseWithoutUser(it) }
    }
}

