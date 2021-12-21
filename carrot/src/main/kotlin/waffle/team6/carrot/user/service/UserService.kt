package waffle.team6.carrot.user.service

import jdk.jshell.spi.ExecutionControl
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.exception.UserAlreadyExistException
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.repository.UserRepository
import waffle.team6.global.common.exception.InvalidRequestException

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
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
    fun updateUser(user: User, updateRequest: UserDto.UpdateRequest) {

        val updatedUser = userRepository.save(user)
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