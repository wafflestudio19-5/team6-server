package waffle.team6.global.auth.kakao

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUserResponse(
    val id: Long,
    @JsonProperty("connected_at")
    val connectedAt: String,
    val properties: KakaoUserProperties,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoUserAccount
)

data class KakaoUserProperties(
    val nickname: String,
    @JsonProperty("profile_image")
    val profileImage: String,
    @JsonProperty("thumbnail_image")
    val thumbnailImage: String
)

data class KakaoUserAccount(
    @JsonProperty("profile_needs_agreement")
    val profileNeedsAgreement: Boolean,
    val profile: KakaoUserProfile,
    @JsonProperty("has_email")
    val hasEmail: Boolean,
    @JsonProperty("is_email_valid")
    val isEmailValid: Boolean,
    @JsonProperty("is_email_verfied")
    val isEmailVerfied: Boolean,
    val email: String,
    @JsonProperty("has_age_range")
    val hasAgeRange: Boolean,
    @JsonProperty("age_range_needs_agreement")
    val ageRangeNeedsAgreement: Boolean,
    @JsonProperty("age_range")
    val ageRange: String,
    @JsonProperty("has_birthday")
    val hasBirthday: Boolean,
    @JsonProperty("birthday_needs_agreement")
    val birthdayNeedsAgreement: Boolean,
    val birthday: String,
    @JsonProperty("birthday_type")
    val birthdayType: String,
    @JsonProperty("has_gender")
    val hasGender: Boolean,
    @JsonProperty("gender_needs_agreement")
    val genderNeedsAgreement: Boolean,
    val gender: String
)

data class KakaoUserProfile(
    val nickname: String,
    @JsonProperty("thumbnail_image_url")
    val thumbnailImageUrl: String,
    @JsonProperty("profile_image_url")
    val profileImageUrl: String
)

