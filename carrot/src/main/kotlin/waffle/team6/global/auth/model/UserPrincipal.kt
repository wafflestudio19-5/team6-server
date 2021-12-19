package waffle.team6.global.auth.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import waffle.team6.carrot.user.model.User
import java.util.*

class UserPrincipal(val user: User): UserDetails {
    override fun getAuthorities(): List<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("normal"))
    }

    override fun getUsername(): String {
        return user.name
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}