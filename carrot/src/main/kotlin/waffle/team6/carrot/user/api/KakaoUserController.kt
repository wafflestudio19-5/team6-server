package waffle.team6.carrot.user.api

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import waffle.team6.carrot.user.dto.SocialLoginDto
import waffle.team6.carrot.user.service.KakaoUserService
import waffle.team6.global.auth.jwt.JwtTokenProvider

@Controller
@RequestMapping("/oauth/kakao/")
class KakaoUserController(
    private val kakaoUserService: KakaoUserService,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @GetMapping
    fun signIn(@RequestParam(required = true) code: String): ResponseEntity<SocialLoginDto.KakaoSignInResponse> {
        val signInResult = kakaoUserService.signIn(code)
        val token = jwtTokenProvider.generateToken(signInResult.name)
        val responseBody = SocialLoginDto.KakaoSignInResponse(
            access_token = token,
            kakaoStatus = signInResult.kakaoStatus.status,
        )

        return ResponseEntity.ok().body(responseBody)
    }

    fun signOut() {
        TODO()
    }
}
