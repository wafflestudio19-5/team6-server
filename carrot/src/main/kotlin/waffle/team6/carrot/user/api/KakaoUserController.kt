package waffle.team6.carrot.user.api

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/oauth/kakao/")
class KakaoUserController {

    @GetMapping
    fun signIn(@RequestParam(required = true) code: String): ResponseEntity<Any> {
        TODO("카카오 서버로 요청, redirect uri")
    }

    fun signUp() {
        TODO()
    }

    fun signOut() {
        TODO()
    }
}
