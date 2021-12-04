package waffle.team6.carrot.user.service

import org.springframework.stereotype.Service
import waffle.team6.carrot.user.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
) {
}