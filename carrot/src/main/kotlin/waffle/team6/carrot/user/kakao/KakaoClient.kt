package waffle.team6.carrot.user.kakao

import feign.codec.Encoder
import feign.form.spring.SpringFormEncoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import waffle.team6.carrot.user.dto.SocialLoginDto
import waffle.team6.carrot.user.kakao.KakaoConf
import waffle.team6.global.config.KakaoClientConfig


@FeignClient(name = "KakaoClient", url = "https://kauth.kakao.com", configuration = [KakaoClientConfig::class])
interface KakaoClient {

    @PostMapping(
        value = ["/oauth/authorize"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getToken(
        @RequestBody body: KakaoTokenRequestTO
    ): ResponseEntity<SocialLoginDto.KakaoTokenResponse>
}
