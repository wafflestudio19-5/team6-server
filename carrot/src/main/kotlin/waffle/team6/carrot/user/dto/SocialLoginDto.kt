package waffle.team6.carrot.user.dto

import waffle.team6.carrot.user.model.KakaoStatus

class SocialLoginDto {
    data class KakaoTokenResponse(
        val access_token: String? = null,
        val token_type: String? = null,
        val refresh_token: String? = null,
        val expires_in: Int? = null,
        val refresh_token_expires_in: Int? = null,
        val scope: String? = null,
    )

    data class KakaoUserInfo(
        val id: Int? = null,
        val kakao_account: KakaoAccountInfo? = null,
        val properties: Properties? = null,
    )

    data class KakaoAccountInfo(
        val profile: Profile? = null,
        val name: String? = null,
        val email: String? = null,
        // TODO: 확장 필요할 수도 있음
    )

    data class Profile(
        val nickname: String? = null,
    )

    data class Properties(
        val nickname: String? = null,
    )

    data class KakaoSignInResult(
        val name: String,
        val is_valid: KakaoStatus,
    )

    data class KakaoSignInResponse(
        val access_token: String? = null,
        val is_valid: KakaoStatus,
    )
}
