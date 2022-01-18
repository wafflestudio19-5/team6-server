package waffle.team6.carrot.user.service

import org.springframework.stereotype.Service
import waffle.team6.carrot.user.client.KakaoClient

@Service
class KakaoUserService(
    private val kakaoClient: KakaoClient,
) {
    fun getToken() {
        TODO()
    }
}
