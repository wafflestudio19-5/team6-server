package waffle.team6.carrot.user.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.product.dto.LikeDto
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.dto.PurchaseRequestDto
import waffle.team6.carrot.product.repository.LikeRepository
import waffle.team6.carrot.product.repository.ProductRepository
import waffle.team6.carrot.product.repository.PurchaseRequestRepository
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.exception.UserAlreadyExistException
import waffle.team6.carrot.user.exception.UserInvalidCurrentPasswordException
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.repository.UserRepository
import waffle.team6.global.config.SecurityConfig

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val purchaseRequestRepository: PurchaseRequestRepository,
    private val productRepository: ProductRepository,
    private val likeRepository: LikeRepository,
) {
    @Transactional
    fun createUser(signUpRequest: UserDto.SignUpRequest): UserDto.Response {
        if (userRepository.findByName(signUpRequest.name) != null) throw UserAlreadyExistException()
        val newUser = User(
            name = signUpRequest.name,
            nickname = signUpRequest.nickname,
            password = passwordEncoder.encode(signUpRequest.password),
            email = signUpRequest.email,
            phone = signUpRequest.phone,
            location = signUpRequest.location,
            rangeOfLocation = signUpRequest.rangeOfLocation,
        )

        return UserDto.Response(userRepository.save(newUser))
    }

    @Transactional
    fun updateUserProfile(user: User, updateProfileRequest: UserDto.UpdateProfileRequest): UserDto.Response {
        user.email = updateProfileRequest.email ?: user.email
        user.phone = updateProfileRequest.phone ?: user.phone
        return UserDto.Response(userRepository.save(user))
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

    // TODO: 자주 쓰는 문구


    fun findMyPurchaseRequests(user: User): List<PurchaseRequestDto.PurchaseRequestResponse> {
        return purchaseRequestRepository.findAllByUser(user).map {
            PurchaseRequestDto.PurchaseRequestResponse(it, true)
        }
    }

    fun findMyProducts(user: User, pageNumber: Int, pageSize: Int): Page<ProductDto.ProductSimpleResponse> {
        return productRepository.findAllByUserId(PageRequest.of(pageNumber, pageSize), user.id).map {
            ProductDto.ProductSimpleResponse(it)
        }
    }

    fun findMyCategoriesOfInterests(user: User): List<String> {
        // TODO
        return listOf("NOT_YET_IMPLEMENTED")
    }

    fun findMyLikes(user: User): List<LikeDto.LikeResponse> {
        return likeRepository.findAllByUser(user).map {
            LikeDto.LikeResponse(it)
        }
    }
}

