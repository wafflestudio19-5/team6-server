package waffle.team6.carrot.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.service.UserService
import waffle.team6.global.auth.CurrentUser
import waffle.team6.global.auth.jwt.JwtTokenProvider
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @PostMapping("/")
    fun signUp(@RequestBody @Valid signUpRequest: UserDto.SignUpRequest): ResponseEntity<UserDto.Response> {
        return ResponseEntity.noContent().header(
                "Authentication",
                jwtTokenProvider.generateToken(
                    userService.createUser(signUpRequest).name))
            .build()
    }

    @GetMapping("/")
    fun getUsers(): ResponseEntity<Any> {
        // TODO implement for admin (유저 Role을 어드민과 일반회원으로 구분할지 논의필요할듯)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/me/")
    fun updateMyProfile(@CurrentUser user: User,
                       @RequestBody @Valid updateProfileRequest: UserDto.UpdateProfileRequest): ResponseEntity<UserDto.Response> {
        userService.updateUserProfile(user, updateProfileRequest)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/me/password/")
    fun updateMyPassword(@CurrentUser user: User,
                         @RequestBody @Valid updatePasswordRequest: UserDto.UpdatePasswordRequest): ResponseEntity<Any> {
        return ResponseEntity.noContent().header(
                "Authentication",
                jwtTokenProvider.generateToken(
                    userService.updateUserPassword(user, updatePasswordRequest).name))
            .build()
    }


    @GetMapping("me/")
    fun getMe(@CurrentUser user: User): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().build()
    }
}