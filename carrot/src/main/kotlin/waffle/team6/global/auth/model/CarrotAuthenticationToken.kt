package waffle.team6.global.auth.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class CarrotAuthenticationToken(
    private val principal: UserPrincipal,
    private var accessToken: Any?,
    authorities: Collection<GrantedAuthority?>? = null,
): AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any {
        TODO("Not yet implemented")
    }

    override fun getPrincipal(): Any {
        TODO("Not yet implemented")
    }
}