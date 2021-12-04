package waffle.team6.carrot.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.model.BuyerProfile
import waffle.team6.carrot.user.model.SellerProfile
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.repository.UserRepository

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun createUser(signUpRequest: UserDto.SignUpRequest): UserDto.Response {

        val newUser = User(
            name = signUpRequest.name,
            password = signUpRequest.password,
            email = signUpRequest.email,
            phone = signUpRequest.phone,
        )

        newUser.buyerProfile = BuyerProfile(user = newUser)
        newUser.sellerProfile = SellerProfile(user = newUser)
        return UserDto.Response(userRepository.save(newUser))
    }

    fun updateUser() {

    }

    fun findMe() {

    }

    fun findMyPurchaseRecords() {

    }

    fun findMyOnePurchaseRecord() {

    }

    fun findMyProducts(){

    }

    fun findMyOneProduct() {

    }
}