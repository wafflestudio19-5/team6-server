package waffle.team6.carrot.user.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.user.kakao.KakaoTokenClient
import waffle.team6.carrot.user.dto.SocialLoginDto
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.exception.UserAlreadyExistException
import waffle.team6.carrot.user.exception.UserKakaoLoginException
import waffle.team6.carrot.user.kakao.KakaoTokenRequestTO
import waffle.team6.carrot.user.kakao.KakaoUserInfoClient
import waffle.team6.carrot.user.model.KakaoStatus
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.repository.UserRepository

@Service
@Transactional
class KakaoUserService(
    private val kakaoTokenClient: KakaoTokenClient,
    private val kakaoUserInfoClient: KakaoUserInfoClient,
    private val userRepository: UserRepository,
) {
    fun signIn(code: String, redirectUri: String): SocialLoginDto.KakaoSignInResult {
        val accessToken = getAccessToken(code, redirectUri).access_token
        val userInfo = kakaoUserInfoClient.getUserInfo("Bearer $accessToken")
        val userId = userInfo.body?.id ?: throw UserKakaoLoginException("토큰을 통해 유저 정보를 받아올 수 없습니다")
        val kakaoUserName = getKakaoUserName(userId)
        val user: User = userRepository.findByName(kakaoUserName) ?: createKakaoUser(kakaoUserName)
        if (user.role == null) user.role = "temporary"
        return SocialLoginDto.KakaoSignInResult(
                name = user.name,
                kakaoStatus = user.kakaoStatus!!,
            )
    }

    fun createKakaoUser(name: String): User {
        val signUpRequest = UserDto.KakaoSignUpRequest(
            name = name,
            nickname = name,
            email = "dummy@dummy.com",
            phone = "010-0000-0000",
            password = "dummyPassword",
            location = "dummyLocation",
            rangeOfLocation = RangeOfLocation.LEVEL_ZERO,
            kakaoStatus = KakaoStatus.INVALID,
        )
        if (userRepository.findByName(signUpRequest.name) != null) throw UserAlreadyExistException()
        val user = User(signUpRequest, "dummyPassword")
        user.role = "temporary"
        return userRepository.save(user)
    }

    fun getAccessToken(codeInput: String, redirectUri: String): SocialLoginDto.KakaoTokenResponse {
        val tokenRequestTO = KakaoTokenRequestTO(code = codeInput, redirect_uri = redirectUri)
        val tokenResponse = kakaoTokenClient.getToken(body = tokenRequestTO)
        if (HttpStatus.OK != tokenResponse.statusCode) {
            throw UserKakaoLoginException()
        }

        return tokenResponse.body ?: throw UserKakaoLoginException("Kakao token REST API returns null")
    }

    fun getKakaoUserName(kakaoId: Int): String {
        return "$kakaoId@kakao"
    }
}
