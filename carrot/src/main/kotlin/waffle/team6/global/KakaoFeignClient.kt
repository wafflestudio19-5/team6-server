package waffle.team6.global

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name="KakaoFeignClient", url="https://kapi.kakao.com/")
interface KakaoFeignClient {
}