package waffle.team6.carrot.user.kakao

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import waffle.team6.carrot.user.dto.SocialLoginDto
import waffle.team6.global.config.KakaoClientConfig

@FeignClient(name = "KakaoUserInfoClient", url = "https://kapi.kakao.com", configuration = [KakaoClientConfig::class])
interface KakaoUserInfoClient {
    @GetMapping(
        value = ["/v2/user/me"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getUserInfo(@RequestHeader Authorization: String): ResponseEntity<SocialLoginDto.KakaoUserInfo>
}