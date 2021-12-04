package waffle.team6.carrot.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.user.dto.UserDto
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
    fun getUsers() {

    }

    @PatchMapping("/me")
    fun updateMe() {

    }

    @GetMapping("/me")
    fun getMe() {

    }

    @GetMapping("/me/buyerProfile/purchaseRecords")
    fun getMyPurchaseRecords() {

    }

    @GetMapping("/me/buyerProfile/purchaseRecords/{purchaseRecordId}")
    fun getMyOnePurchaseRecord(@PathVariable purchaseRecordId: Long){

    }

    @GetMapping("/me/sellerProfile/products")
    fun getMyProducts() {

    }

    @GetMapping("/me/sellerProfile/products/{productId}")
    fun getMyOneProduct(@PathVariable productId: Long) {

    }
}