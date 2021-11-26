package waffle.team6.global.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class JwtTokenUtils(
    @Value("\${jwt.jwt-secret-key}") private val secretKey: String,
    @Value("\${jwt.jwt-expiration-in-ms}") private val expiration: Long
) {


}