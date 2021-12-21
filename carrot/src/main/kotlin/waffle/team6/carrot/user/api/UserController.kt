package waffle.team6.carrot.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.service.UserService
import waffle.team6.global.auth.CurrentUser
import waffle.team6.global.auth.jwt.JwtTokenProvider

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @PostMapping("/")
    fun signUp(@RequestBody signUpRequest: UserDto.SignUpRequest): ResponseEntity<UserDto.Response> {
        return ResponseEntity.noContent().header(
                "Authentication",
                jwtTokenProvider.generateToken(
                    userService.createUser(signUpRequest).name))
            .build()
    }

    @GetMapping("/")
    fun getUsers(): ResponseEntity<Any> {
        // TODO implement for admin
        return ResponseEntity.ok().build()
    }

    @PatchMapping("me/")
    fun updateMe(
        @CurrentUser user: User,
        @RequestBody updateRequest: UserDto.UpdateRequest): ResponseEntity<UserDto.Response> {
        userService.updateUser(user, updateRequest)
        return ResponseEntity.ok().build()
    }

    @GetMapping("me/")
    fun getMe(@CurrentUser user: User): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().build()
    }
}