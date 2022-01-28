package waffle.team6.carrot.user.kakao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value

data class KakaoTokenRequestTO(
    @JsonProperty("grant_type")
    var grant_type: String? = KakaoConf.GRANT_TYPE,

    @JsonProperty("client_id")
    var client_id: String? = KakaoConf.CLIENT_ID,

    var code: String? = null,
) {
    @JsonProperty("redirect_uri")
    @Value("\${front.redirect-uri}")
    lateinit var redirect_uri: String
}