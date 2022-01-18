package waffle.team6.carrot.user.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import waffle.team6.carrot.user.dto.SocialLoginDto
import waffle.team6.carrot.user.kakao.KakaoConf

@FeignClient(name = "KakaoClient", url = "")
interface KakaoClient {

    @PostMapping
    fun getToken(
        @RequestParam client_id: String = KakaoConf.CLIENT_ID,
        @RequestParam redirect_uri: String = KakaoConf.REDIRECT_URI,
        @RequestParam code: String = KakaoConf.REDIRECT_URI
    ): ResponseEntity<SocialLoginDto.KakaoTokenResponse>
}
