package waffle.team6.carrot.user.api

import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.product.dto.LikeDto
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.dto.PurchaseRequestDto
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.service.UserService
import waffle.team6.global.auth.CurrentUser
import waffle.team6.global.auth.jwt.JwtTokenProvider
import javax.validation.Valid
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

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
                userService.createUser(signUpRequest).name
            )
        )
            .build()
    }

    @GetMapping("/")
    fun getUsers(): ResponseEntity<Any> {
        // TODO implement for admin (유저 Role을 어드민과 일반회원으로 구분할지 논의필요할듯)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/me/")
    fun updateMyProfile(
        @CurrentUser user: User,
        @RequestBody @Valid updateProfileRequest: UserDto.UpdateProfileRequest
    ): ResponseEntity<UserDto.Response> {
        userService.updateUserProfile(user, updateProfileRequest)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/me/password/")
    fun updateMyPassword(
        @CurrentUser user: User,
        @RequestBody @Valid updatePasswordRequest: UserDto.UpdatePasswordRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.noContent().header(
            "Authentication",
            jwtTokenProvider.generateToken(
                userService.updateUserPassword(user, updatePasswordRequest).name
            )
        )
            .build()
    }

    // TODO: 내 동네 인증


    @GetMapping("/me/")
    fun getMe(@CurrentUser user: User): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().body(userService.findMe(user))
    }

    @GetMapping("/{userId}/")
    fun getUser(@CurrentUser user: User): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().body(userService.findMe(user))
    }

    @GetMapping("/duplicate/")
    fun checkDuplicatedNameForSignUp(@RequestParam name: String): ResponseEntity<Boolean> {
        return ResponseEntity.ok().body(userService.isUserNameDuplicated(name))
    }

    @GetMapping("/me/purchase-requests/")
    fun getMyPurchaseRequests(@CurrentUser user: User): ResponseEntity<List<PurchaseRequestDto.PurchaseRequestResponseWithoutUser>> {
        return ResponseEntity.ok().body(userService.findMyPurchaseRequests(user))
    }

    @GetMapping("/me/products/")
    fun getMyProducts(
        @CurrentUser user: User,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int
    ): ResponseEntity<Page<ProductDto.ProductSimpleResponseWithoutUser>> {
        return ResponseEntity.ok().body(userService.findMyProducts(user, pageNumber, pageSize))
    }

    @GetMapping("/me/likes/")
    fun getMyLikes(
        @CurrentUser user: User,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int
    ): ResponseEntity<Page<LikeDto.LikeResponse>> {
        return ResponseEntity.ok().body(userService.findMyLikes(user, pageNumber, pageSize))
    }

    @GetMapping("/me/categoryOfInterest/")
    fun getMyCategoryOfInterest(@CurrentUser user: User): ResponseEntity<List<Any>> {
        return ResponseEntity.ok().body(userService.findMyCategoriesOfInterests(user))
    }
}