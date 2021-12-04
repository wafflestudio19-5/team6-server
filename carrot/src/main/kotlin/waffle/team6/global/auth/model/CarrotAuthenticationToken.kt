package waffle.team6.global.auth.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class CarrotAuthenticationToken(
    private val principal: UserPrincipal,
    private var accessToken: Any?,
    authorities: Collection<GrantedAuthority?>? = null,
): AbstractAuthenticationToken(authorities) {
    init {
        if (authorities == null)
            super.setAuthenticated(false)
        else super.setAuthenticated(true)
    }

    override fun getCredentials(): Any? {
        return accessToken
    }

    override fun getPrincipal(): Any? {
        return principal
    }
    override fun eraseCredentials() {
        super.eraseCredentials()
        accessToken = null
    }
}