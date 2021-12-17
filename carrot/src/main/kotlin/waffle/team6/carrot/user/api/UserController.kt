package waffle.team6.carrot.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.service.UserService
import waffle.team6.global.auth.CurrentUser
import waffle.team6.global.auth.jwt.JwtTokenProvider

@RestController
@RequestMapping("/api/v1/users/")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @PostMapping
    fun signUp(@RequestBody signUpRequest: UserDto.SignUpRequest): ResponseEntity<UserDto.Response> {
        return ResponseEntity.noContent().header(
                "Authentication",
                jwtTokenProvider.generateToken(
                    userService.createUser(signUpRequest).name))
            .build()
    }

    @GetMapping
    fun getUsers(): ResponseEntity<Any> {
        // TODO implement for admin
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/me/")
    fun updateMe(@CurrentUser user: User): ResponseEntity<UserDto.Response> {

        return ResponseEntity.ok().build()
    }

    @GetMapping("/me/")
    fun getMe(@CurrentUser user: User): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me/buyerProfile/purchaseRecords/")
    fun getMyPurchaseRecords(@CurrentUser user: User): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me/buyerProfile/purchaseRecords/{purchaseRecordId}/")
    fun getMyOnePurchaseRecord(@CurrentUser user: User, @PathVariable purchaseRecordId: Long): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me/sellerProfile/products/")
    fun getMyProducts(@CurrentUser user: User, ): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me/sellerProfile/products/{productId}/")
    fun getMyOneProduct(@PathVariable productId: Long): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }
}