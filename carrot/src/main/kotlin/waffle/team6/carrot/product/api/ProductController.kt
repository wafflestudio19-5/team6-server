package waffle.team6.carrot.product.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.dto.PurchaseRequestDto
import waffle.team6.carrot.product.model.Category
import waffle.team6.carrot.product.service.ProductService
import waffle.team6.global.auth.CurrentUser
import waffle.team6.carrot.user.model.User
import javax.validation.Valid
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@Validated
@RestController
@RequestMapping("/api/v1/products")
class ProductController (
    private val productService: ProductService
    ) {
    @GetMapping("/")
    @Operation(summary = "전체 판매글 조회/검색", description = "전체 판매글을 조회하거나 검색합니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "400", description = "pageNumber, pageSize, title, price에 올바르지 않은 값이 들어간 경우"),
        ApiResponse(responseCode = "0001", description = "카테고리 값에 1이상 17이하가 아닌 수가 들어간 경우"),
        ApiResponse(responseCode = "0002", description = "동네범위 값에 0이상 3이하가 아닌 수가 들어간 경우")
    ])
    fun getProducts(
        @CurrentUser @ApiIgnore user: User,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) rangeOfLocation: Int?,
        @RequestParam(required = false) categories: List<Int>?,
        @RequestParam(required = false) @Positive minPrice: Long?,
        @RequestParam(required = false) @Positive maxPrice: Long?,
    ): ResponseEntity<Page<ProductDto.ProductSimpleResponse>> {
        return if (title.isNullOrBlank()) ResponseEntity.ok().body(productService.getProducts(user, pageNumber, pageSize))
        else ResponseEntity.ok().body(productService.searchProducts(user, ProductDto.ProductSearchRequest(
                pageNumber,
                pageSize,
                title,
                rangeOfLocation?.let { RangeOfLocation.from(it) },
                categories?.map { Category.from(it) },
                minPrice,
                maxPrice
        )))
    }

    @PostMapping("/")
    @Operation(summary = "판매글 등록", description = "판매글을 등록합니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "400", description = "Request Body 에 누락이나 잘못된 값이 들어간 경우"),
        ApiResponse(responseCode = "3303", description = "현재 사용자가 올린 이미지가 아닌 경우"),
        ApiResponse(responseCode = "9201", description = "이미지가 이미 다른 판매글에 등록된 경우")
    ])
    fun addProduct(
        @CurrentUser @ApiIgnore user: User,
        @RequestBody @Valid productPostRequest: ProductDto.ProductPostRequest
    ): ResponseEntity<ProductDto.ProductResponse> {
        return ResponseEntity.ok().body(productService.addProduct(user, productPostRequest))
    }

    @GetMapping("/{product_id}/")
    @Operation(summary = "개별 판매글 조회", description = "개별 판매글을 조회합니다. 조회수가 1 증가합니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우"),
        ApiResponse(responseCode = "3212", description = "판매자가 아닌 사용자가 숨긴 게시물을 보려한 경우")
    ])
    fun getProduct(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<ProductDto.ProductResponse> {
        return ResponseEntity.ok().body(productService.getProduct(user, productId))
    }

    @DeleteMapping("/{product_id}/")
    @Operation(summary = "판매글 삭제", description = "판매글이 삭제됩니다", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "3202", description = "판매자가 아닌 다른 사용자가 삭제 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun deleteProduct(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.deleteProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{product_id}/")
    @Operation(summary = "판매글 수정", description = "판매글의 상세 정보를 수정합니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0201", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "3201", description = "판매자가 아닌 다른 사용자가 수정 요청을 시도한 경우"),
        ApiResponse(responseCode = "3303", description = "현재 사용자가 올린 이미지가 아닌 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우"),
        ApiResponse(responseCode = "9201", description = "이미지가 이미 다른 판매글에 등록된 경우")
    ])
    fun updateProduct(
        @CurrentUser @ApiIgnore user: User,
        @RequestBody @Valid productPatchRequest: ProductDto.ProductUpdateRequest,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<ProductDto.ProductResponse> {
        return ResponseEntity.ok().body(productService.patchProduct(user, productPatchRequest, productId))
    }

    @PostMapping("/{product_id}/likes/")
    @Operation(summary = "판매글 관심목록 등록", description = "판매글을 관심목록에 등록합니다", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "0201", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "3207", description = "판매자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun likeProduct(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.likeProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{product_id}/likes/")
    @Operation(summary = "판매글 관심목록 해제", description = "판매글을 관심목록에서 해제합니다", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun cancelLikeProduct(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.cancelLikeProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{product_id}/purchases/")
    @Operation(summary = "구매 요청", description = "해당 판매글에 대해 구매 요청을 보냅니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0201", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "3208", description = "판매자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우"),
        ApiResponse(responseCode = "9202", description = "해당 판매글에 이미 구매 요청을 보낸 경우")
    ])
    fun chat(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @RequestBody @Valid purchaseRequest: PurchaseRequestDto.PurchaseRequest
    ): ResponseEntity<PurchaseRequestDto.PurchaseRequestResponse> {
        return ResponseEntity.ok().body(productService.chat(user, productId, purchaseRequest))
    }

    @PutMapping("/{product_id}/status/")
    @Operation(summary = "판매글 상태를 변경합니다", description = "action으로 넣을 수 있는 값은 다음과 같습니다:" +
            "reserved(예약중), sold out(판매완료), for sale(판매중), hide(숨김), show(다시 노출), bump(끌올)", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "0201", description = "해당 판매글이 이미 판매완료인 경우 (hide/show 는 상관없음)"),
        ApiResponse(responseCode = "0203", description = "마지막으로 끌올(생성)한 지 하루가 지나지 않았을 경우 (bump 요청)"),
        ApiResponse(responseCode = "3203", description = "판매자가 아닌 다른 사용자가 reserved/sold out/for sale 요청을 시도한 경우"),
        ApiResponse(responseCode = "3209", description = "판매자가 아닌 다른 사용자가 hide 요청을 시도한 경우"),
        ApiResponse(responseCode = "3210", description = "판매자가 아닌 다른 사용자가 show 요청을 시도한 경우"),
        ApiResponse(responseCode = "3211", description = "판매자가 아닌 다른 사용자가 bump 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun changeProductStatus(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @RequestBody @Valid productStatusUpdateRequest: ProductDto.ProductStatusUpdateRequest
    ): ResponseEntity<Any> {
        when (productStatusUpdateRequest.action) {
            "reserved" -> productService.changeProductStatusToReserved(user, productId)
            "sold out" -> productService.changeProductStatusToSoldOut(user, productId)
            "for sale" -> productService.changeProductStatusToForSale(user, productId)
            "hide" -> productService.hideProduct(user, productId)
            "show" -> productService.showProduct(user, productId)
            "bump" -> productService.bringUpMyPost(user, productId)
        }
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{product_id}/purchases/")
    @Operation(summary = "구매 요청 조회", description = "해당 판매글에 대한 모든 구매 요청이 조회됩니다. 가격 제안이 있는 구매 요청을 따로 볼 수 있습니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "3205", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun getPurchaseRequests(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int,
        @RequestParam(required = false) withPriceSuggestion: Boolean
    ): ResponseEntity<Page<PurchaseRequestDto.PurchaseRequestResponse>> {
        return if (withPriceSuggestion) ResponseEntity.ok().body(productService
            .getProductPurchaseRequestsWithPriceSuggestion(user, productId, pageNumber, pageSize))
        else ResponseEntity.ok().body(productService.getProductPurchaseRequests(user, productId, pageNumber, pageSize))
    }

    @GetMapping("/{product_id}/purchases/{purchase_request_id}/")
    @Operation(summary = "개별 구매 요청 조회", description = "해당 판매글에 대한 개별 구매 요청이 조회됩니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0202", description = "해당 판매글에 대한 구매 요청이 아닌 경우"),
        ApiResponse(responseCode = "3205", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4201", description = "해당 구매 요청이 없는 경우")
    ])
    fun getPurchaseRequest(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): ResponseEntity<PurchaseRequestDto.PurchaseRequestResponse> {
        return ResponseEntity.ok().body(productService.getProductPurchaseRequest(user, productId, purchaseRequestId))
    }

    @PutMapping("/{product_id}/purchases/{purchase_request_id}/")
    @Operation(summary = "구매 요청 다시 보내기", description = "해당 구매 요청을 바꿔서 다시 보냅니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0201", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "0202", description = "해당 판매글에 대한 구매 요청이 아닌 경우"),
        ApiResponse(responseCode = "3213", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우"),
        ApiResponse(responseCode = "4201", description = "해당 구매 요청이 없는 경우")
    ])
    fun chatAgain(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long,
        @RequestBody @Valid purchaseRequest: PurchaseRequestDto.PurchaseRequest
    ): ResponseEntity<PurchaseRequestDto.PurchaseRequestResponse> {
        return ResponseEntity.ok().body(productService.chatAgain(user, productId, purchaseRequestId, purchaseRequest))
    }

    @PutMapping("/{product_id}/purchases/{purchase_request_id}/approval/")
    @Operation(summary = "구매 수락/거절", description = "해당 판매글에 대한 구매 요청이 확정됩니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0201", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "0202", description = "해당 판매글에 대한 구매 요청이 아닌 경우"),
        ApiResponse(responseCode = "0204", description = "해당 판매요청을 이미 거절한 경우"),
        ApiResponse(responseCode = "3206", description = "판매자가 아닌 다른 사용자가 수락을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우"),
        ApiResponse(responseCode = "4201", description = "해당 판매 요청이 없는 경우")
    ])
    fun confirmPurchaseRequest(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long,
        @RequestBody @Valid approval: ProductDto.ProductPurchaseRequestApprovalRequest
    ): ResponseEntity<PurchaseRequestDto.PurchaseRequestResponse> {
        return if (approval.accepted) ResponseEntity.ok()
            .body(productService.confirmProductPurchaseRequest(user, productId, purchaseRequestId))
        else return ResponseEntity.ok()
            .body(productService.rejectProductPurchaseRequest(user, productId, purchaseRequestId))
    }
}