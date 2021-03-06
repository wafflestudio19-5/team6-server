package waffle.team6.global.auth.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import waffle.team6.carrot.user.exception.UserNotActiveException
import waffle.team6.carrot.user.repository.UserRepository
import waffle.team6.global.auth.model.UserPrincipal

@Service
class UserPrincipalDetailService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(s: String): UserDetails {
        val user = userRepository.findByName(s) ?: throw UsernameNotFoundException("User with name '%s' not found")
        if (!user.isActive) throw UserNotActiveException()
        return UserPrincipal(user)
    }
}