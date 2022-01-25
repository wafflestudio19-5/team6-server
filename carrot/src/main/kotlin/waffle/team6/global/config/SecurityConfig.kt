package waffle.team6.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import waffle.team6.global.auth.filter.JwtAuthenticationFilter
import waffle.team6.global.auth.filter.SigninAuthenticationFilter
import waffle.team6.global.auth.jwt.JwtAuthenticationEntryPoint
import waffle.team6.global.auth.jwt.JwtTokenProvider
import waffle.team6.global.auth.service.UserPrincipalDetailService

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userPrincipalDetailService: UserPrincipalDetailService
) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(daoAuthenticationProvider())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder())
        provider.setUserDetailsService(userPrincipalDetailService)
        return provider
    }

    override fun configure(http: HttpSecurity) {
        http
            .httpBasic().disable()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .addFilter(SigninAuthenticationFilter(authenticationManager(), jwtTokenProvider))
            .addFilter(JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider))
            .authorizeRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs").permitAll() // swagger docs
            .antMatchers("/ping-test/").permitAll()  // ping test
            .antMatchers("/api/v1/users/signin/").permitAll()  // Auth entrypoint
            .antMatchers(HttpMethod.GET, "/api/v1/users/duplicate/").permitAll()  // Auth entrypoint
            .antMatchers(HttpMethod.GET, "/oauth/kakao/").permitAll()  // Auth entrypoint
            .antMatchers(HttpMethod.POST, "/api/v1/users/").anonymous()  // SignUp user
            .anyRequest().authenticated()
    }

    @Bean
    public fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration: CorsConfiguration = CorsConfiguration();

        configuration.setAllowedOriginPatterns(
            listOf("http://localhost:3000", "http://localhost:3001",
                "https://carrotshop.shop", "http://carrotshop.shop",
                "https://www.carrotshop.shop", "http://www.carrotshop.shop",
                "https://carrotshop.shop:[*]", "http://carrotshop.shop:[*]"))
        configuration.addAllowedHeader("*")
        configuration.setExposedHeaders(listOf("Authentication"))
        configuration.addAllowedMethod("*")
        configuration.setAllowCredentials(true)

        val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
