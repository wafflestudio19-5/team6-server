package waffle.team6.global.auth.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import waffle.team6.carrot.user.repository.UserRepository
import waffle.team6.global.auth.model.UserPrincipal

@Service
class UserPrincipalDetailService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(s: String): UserDetails {
        val user = userRepository.findByEmail(s) ?: throw UsernameNotFoundException("User with email '%s' not found")
        return UserPrincipal(user)
    }
}