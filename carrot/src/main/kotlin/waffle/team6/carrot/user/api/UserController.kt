package waffle.team6.carrot.user.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import waffle.team6.carrot.product.dto.LikeDto
import waffle.team6.carrot.product.dto.PhraseDto
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.purchaseOrders.dto.PurchaseOrderDto
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
    @Operation(summary = "회원가입", description = "회원가입", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "9100", description = "해당 판매글이 이미 판매완료인 경우"),
    ])
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
    @Operation(summary = "회원검색", description = "회원검색", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
    ])
    fun searchUser(
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int,
        @RequestParam(required = true) name: String
    ): ResponseEntity<Page<UserDto.UserSimpleResponse>> {
        return ResponseEntity.ok().body(userService.findUser(pageNumber, pageSize, name))
    }

    @PatchMapping("/me/")
    @Operation(summary = "프로필 정보 수정", description = "프로필 정보 수정", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
    ])
    fun updateMyProfile(
        @CurrentUser @ApiIgnore user: User,
        @RequestBody @Valid updateProfileRequest: UserDto.UpdateProfileRequest
    ): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().body(userService.updateUserProfile(user, updateProfileRequest))
    }

    @DeleteMapping("/me/")
    @Operation(summary = "계정 삭제", description = "판매글 및 거래가 성사된 구매 요청 외 나머지 정보는 삭제됩니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
    ])
    fun deleteMyAccount(@CurrentUser @ApiIgnore user: User): ResponseEntity<Any> {
        return ResponseEntity.ok().body(userService.deleteMyAccount(user))
    }

    @PatchMapping("/me/password/")
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "0101", description = "비밀번호가 틀린 경우"),
    ])
    fun updateMyPassword(
        @CurrentUser @ApiIgnore user: User,
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
    @Operation(summary = "내 프로필 조회", description = "내 프로필 조회", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
    ])
    fun getMe(@CurrentUser @ApiIgnore user: User): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().body(userService.findMe(user))
    }

    @GetMapping("/{userId}/")
    @Operation(summary = "다른 유저 프로필 조회", description = "다른 유저 프로필 조회", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "4100", description = "해당 id를 가진 회원이 없는 경우"),
    ])
    fun getUser(@PathVariable userId: Long): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().body(userService.findWithId(userId))
    }

    @GetMapping("/{user_id}/products/")
    @Operation(summary = "유저 판매글 조회", description = "status로 가능한 값: for-sale,sold-out,all", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "400", description = "pageNumber, pageSize, status가 올바르지 않은 경우"),
        ApiResponse(responseCode = "0004", description = "status가 올바르지 않은 경우"),
    ])
    fun getUserProducts(
        @PathVariable("user_id") userId: Long,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int,
        @RequestParam(required = true) status: String
    ): ResponseEntity<Page<ProductDto.ProductSimpleResponseWithoutUser>> {
        return ResponseEntity.ok().body(userService.findUserProducts(userId, pageNumber, pageSize, status))
    }

    @GetMapping("/duplicate/")
    @Operation(summary = "아이디 중복 체크", description = "아이디 중복 체크", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
    ])
    fun checkDuplicatedNameForSignUp(@RequestParam name: String): ResponseEntity<Boolean> {
        return ResponseEntity.ok().body(userService.isUserNameDuplicated(name))
    }

    @GetMapping("/me/purchase-orders/")
    @Operation(summary = "내 구매 요청 조회", description = "status로 가능한 값: pending,accepted,rejected", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "400", description = "pageNumber, pageSize, status가 올바르지 않은 경우"),
        ApiResponse(responseCode = "0004", description = "status가 올바르지 않은 경우"),
    ])
    fun getMyPurchaseRequests(
        @CurrentUser @ApiIgnore user: User,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int,
        @RequestParam(required = true) status: String
    ): ResponseEntity<Page<PurchaseOrderDto.PurchaseOrderResponseWithoutUser>> {
        return ResponseEntity.ok().body(userService.findMyPurchaseRequests(user, pageNumber, pageSize, status))
    }

    @GetMapping("/me/products/")
    @Operation(summary = "내 판매글 조회", description = "status로 가능한 값: for-sale,sold-out,hidden", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "400", description = "pageNumber, pageSize, status가 올바르지 않은 경우"),
        ApiResponse(responseCode = "0004", description = "status가 올바르지 않은 경우"),
    ])
    fun getMyProducts(
        @CurrentUser @ApiIgnore user: User,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int,
        @RequestParam(required = true) status: String
    ): ResponseEntity<Page<ProductDto.ProductSimpleResponseWithoutUser>> {
        return ResponseEntity.ok().body(userService.findMyProducts(user, pageNumber, pageSize, status))
    }

    @GetMapping("/me/likes/")
    @Operation(summary = "내 관심목록 조회", description = "내 관심목록 조회", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "400", description = "pageNumber, pageSize가 올바르지 않은 경우"),
    ])
    fun getMyLikes(
        @CurrentUser @ApiIgnore user: User,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int
    ): ResponseEntity<Page<LikeDto.LikeResponse>> {
        return ResponseEntity.ok().body(userService.findMyLikes(user, pageNumber, pageSize))
    }

    @GetMapping("/me/categoryOfInterest/")
    @Operation(summary = "내 관심 카테고리 조회", description = "내 관심 카테고리 조회", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
    ])
    fun getMyCategoryOfInterest(@CurrentUser @ApiIgnore user: User): ResponseEntity<List<Any>> {
        return ResponseEntity.ok().body(userService.findMyCategoriesOfInterests(user))
    }

    @PostMapping("/me/phrases/")
    @Operation(summary = "자주 쓰는 문구 저장", description = "자주 쓰는 문구 저장", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
    ])
    fun addMyPhrase(@CurrentUser @ApiIgnore user: User, @RequestBody @Valid phrase: PhraseDto.PhrasePostRequest
    ): ResponseEntity<PhraseDto.PhraseResponse> {
        return ResponseEntity.ok().body(userService.addMyPhrase(user, phrase))
    }

    @DeleteMapping("/me/phrases/{index}/")
    @Operation(summary = "자주 쓰는 문구 삭제", description = "인덱스는 0부터 시작합니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
    ])
    fun deleteMyPhrase(@CurrentUser @ApiIgnore user: User, @PathVariable index: Int): ResponseEntity<Any> {
        return ResponseEntity.ok().body(userService.deleteMyPhrase(user, index))
    }

    @GetMapping("/me/phrases/")
    @Operation(summary = "자주 쓰는 문구 조회", description = "자주 쓰는 문구 조회", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
    ])
    fun getMyPhrases(@CurrentUser @ApiIgnore user: User): ResponseEntity<PhraseDto.PhraseResponse> {
        return ResponseEntity.ok().body(userService.getMyPhrases(user))
    }
}