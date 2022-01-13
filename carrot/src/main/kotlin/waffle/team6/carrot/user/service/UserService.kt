package waffle.team6.carrot.user.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.product.dto.LikeDto
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.purchaseOrders.dto.PurchaseOrderDto
import waffle.team6.carrot.product.model.CategoryOfInterest
import waffle.team6.carrot.product.repository.CategoryOfInterestRepository
import waffle.team6.carrot.product.repository.LikeRepository
import waffle.team6.carrot.product.repository.ProductRepository
import waffle.team6.carrot.purchaseOrders.repository.PurchaseOrderRepository
import waffle.team6.carrot.user.dto.UserDto
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
) {
    @Transactional
    fun createUser(signUpRequest: UserDto.SignUpRequest): UserDto.Response {
        if (userRepository.findByName(signUpRequest.name) != null) throw UserAlreadyExistException()
        return UserDto.Response(userRepository.save(User(signUpRequest, passwordEncoder.encode(signUpRequest.password))))
    }

    @Transactional
    fun updateUserProfile(user: User, updateProfileRequest: UserDto.UpdateProfileRequest): UserDto.Response {
        return UserDto.Response(user.modifyProfile(updateProfileRequest))
    }

    @Transactional
    fun updateUserPassword(user: User, updatePasswordRequest: UserDto.UpdatePasswordRequest): UserDto.Response {
        if (!passwordEncoder.matches(updatePasswordRequest.currentPassword, user.password)) {
            throw UserInvalidCurrentPasswordException("Incorrect Current Password")
        }
        user.password = passwordEncoder.encode(updatePasswordRequest.newPassword)
        return UserDto.Response(userRepository.save(user))
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

    // TODO: 자주 쓰는 문구
    fun addMyPhrase() {

    }

    fun getMyPhrases() {

    }


    fun findMyPurchaseRequests(user: User): List<PurchaseOrderDto.PurchaseOrderResponseWithoutUser> {
        return purchaseOrderRepository.findAllByUser(user).map {
            PurchaseOrderDto.PurchaseOrderResponseWithoutUser(it)
        }
    }

    fun findMyProducts(user: User, pageNumber: Int, pageSize: Int): Page<ProductDto.ProductSimpleResponseWithoutUser> {
        return productRepository.findAllByUserId(PageRequest.of(pageNumber, pageSize, Sort.by("lastBringUpMyPost").descending()), user.id).map {
            ProductDto.ProductSimpleResponseWithoutUser(it)
        }
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
}

