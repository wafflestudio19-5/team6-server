package waffle.team6.global.auth

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
    @Value("\${jwt.header}") private val header: String
):OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    private val tokenPrefix = "Bearer "

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestHeader: String? = request.getHeader(this.header)

        if (requestHeader != null && requestHeader.startsWith(tokenPrefix)) {

            val authToken = requestHeader.substring(tokenPrefix.length)
            //TODO implement
        }

        filterChain.doFilter(request, response)
    }

}