package waffle.team6.carrot.user.api

import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.user.service.UserService

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun signUp() {

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