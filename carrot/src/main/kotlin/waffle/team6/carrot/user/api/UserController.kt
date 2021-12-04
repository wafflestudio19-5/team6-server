package waffle.team6.carrot.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.service.UserService

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun signUp(@RequestBody signUpRequest: UserDto.SignUpRequest): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok(userService.createUser(signUpRequest))
    }

    @GetMapping
    fun getUsers(): ResponseEntity<Any> {
        // TODO implement for admin
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/me")
    fun updateMe(user: User): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me")
    fun getMe(): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me/buyerProfile/purchaseRecords")
    fun getMyPurchaseRecords(): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me/buyerProfile/purchaseRecords/{purchaseRecordId}")
    fun getMyOnePurchaseRecord(@PathVariable purchaseRecordId: Long): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me/sellerProfile/products")
    fun getMyProducts(): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/me/sellerProfile/products/{productId}")
    fun getMyOneProduct(@PathVariable productId: Long): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }
}