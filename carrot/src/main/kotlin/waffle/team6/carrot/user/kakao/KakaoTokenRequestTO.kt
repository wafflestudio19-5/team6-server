package waffle.team6.carrot.user.kakao

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoTokenRequestTO(
    @JsonProperty("redirect_uri")
    var redirect_uri: String? = KakaoConf.REDIRECT_URI,

    @JsonProperty("grant_type")
    var grant_type: String? = KakaoConf.GRANT_TYPE,

    @JsonProperty("client_id")
    var client_id: String? = KakaoConf.CLIENT_ID,

    var code: String? = null,
)