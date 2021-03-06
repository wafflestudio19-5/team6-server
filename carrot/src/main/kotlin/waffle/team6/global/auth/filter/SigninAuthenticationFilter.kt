package waffle.team6.global.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import waffle.team6.global.auth.dto.LoginRequest
import waffle.team6.global.auth.jwt.JwtTokenProvider
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SigninAuthenticationFilter(
    authenticationManager: AuthenticationManager?,
    private val jwtTokenProvider: JwtTokenProvider,
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    init {
        setRequiresAuthenticationRequestMatcher(
            AntPathRequestMatcher("/api/v1/users/signin/", "POST"))
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val generatedToken = jwtTokenProvider.generateToken(authResult)
        response.addHeader("Authentication", generatedToken)

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.writer.write("{\"access_token\": \"" + generatedToken + "\"}")

        response.status = HttpServletResponse.SC_OK
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        super.unsuccessfulAuthentication(request, response, failed);
        response.status = HttpServletResponse.SC_UNAUTHORIZED
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val parsedRequest: LoginRequest = parseRequest(request)
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(parsedRequest.name, parsedRequest.password))
    }

    private fun parseRequest(request: HttpServletRequest): LoginRequest {
        return ObjectMapper()
            .readValue(request.reader, LoginRequest::class.java)
    }

}
