package waffle.team6.carrot.user.api

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import waffle.team6.carrot.user.service.KakaoUserService

@Controller
@RequestMapping("/oauth/kakao/")
class KakaoUserController(
    private val kakaoUserService: KakaoUserService,
) {

    @GetMapping
    fun signIn(@RequestParam(required = true) code: String): ResponseEntity<Any> {
        kakaoUserService.getTokenWithCode(code)
        return ResponseEntity.ok().build()
    }

    fun signUp() {
        TODO()
    }

    fun signOut() {
        TODO()
    }
}
