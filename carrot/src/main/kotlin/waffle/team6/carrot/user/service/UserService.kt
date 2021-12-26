package waffle.team6.carrot.user.service

import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
    private val securityConfig: SecurityConfig,
) {
    @Transactional
    fun createUser(signUpRequest: UserDto.SignUpRequest): UserDto.Response {
        if (userRepository.findByName(signUpRequest.name) != null) throw UserAlreadyExistException()
        val newUser = User(
            name = signUpRequest.name,
            password = passwordEncoder.encode(signUpRequest.password),
            email = signUpRequest.email,
            phone = signUpRequest.phone,
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
        return UserDto.Response(
            user
        )
    }

    fun findMyPurchaseRecords(user: User) {

    }

    fun findMyOnePurchaseRecord() {

    }

    fun findMyProducts(){

    }

    fun findMyOneProduct() {

    }
}