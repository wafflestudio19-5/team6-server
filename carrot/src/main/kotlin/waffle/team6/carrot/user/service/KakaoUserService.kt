package waffle.team6.carrot.user.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import waffle.team6.carrot.user.kakao.KakaoClient
import waffle.team6.carrot.user.dto.SocialLoginDto
import waffle.team6.carrot.user.kakao.KakaoConf
import waffle.team6.carrot.user.kakao.KakaoTokenRequestTO

@Service
class KakaoUserService(
    private val kakaoClient: KakaoClient,
) {
    fun getTokenWithCode(codeInput: String): ResponseEntity<SocialLoginDto.KakaoTokenResponse> {
        val tokenRequestTO = KakaoTokenRequestTO(code = codeInput)
        val tokenResponse = kakaoClient.getToken(body = tokenRequestTO)
        println("tokenResponse: $tokenResponse")

        return tokenResponse
    }
}
