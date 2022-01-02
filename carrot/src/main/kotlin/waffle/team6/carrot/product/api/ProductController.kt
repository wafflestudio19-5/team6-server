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
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@Validated
@RestController
@RequestMapping("/api/v1/products")
class ProductController (
    private val productService: ProductService
    ) {
    @GetMapping("/")
    @Operation(summary = "전체 판매글 조회", description = "전체 판매글을 조회합니다. 아직 페이지네이션, 지역정보 기능을 제공하지 않습니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response")
    ])
    fun getProducts(
        @CurrentUser @ApiIgnore user: User,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int
    ): ResponseEntity<Page<ProductDto.ProductSimpleResponse>> {
        return ResponseEntity.ok().body(productService.getProducts(user, pageNumber, pageSize))
    }

    @GetMapping("/search/")
    fun searchProducts(
        @CurrentUser @ApiIgnore user: User,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int,
        @RequestParam(required = true) @NotBlank title: String,
        @RequestParam(required = false) rangeOfLocation: Int?,
        @RequestParam(required = false) categories: List<Int>,
        @RequestParam(required = false) @Positive minPrice: Long?,
        @RequestParam(required = false) @Positive maxPrice: Long?,
    ): ResponseEntity<Page<ProductDto.ProductSimpleResponse>> {
        val searchRequest = ProductDto.ProductSearchRequest(
            pageNumber,
            pageSize,
            title,
            rangeOfLocation?.let { RangeOfLocation.from(it) },
            categories.map { Category.from(it) },
            minPrice,
            maxPrice
        )
        return ResponseEntity.ok().body(productService.searchProducts(user, searchRequest))
    }

    @PostMapping("/")
    @Operation(summary = "판매글 등록", description = "판매글을 등록합니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "400", description = "Request Body 에 누락이나 잘못된 값이 들어간 경우")
    ])
    fun addProducts(
        @CurrentUser @ApiIgnore user: User,
        @RequestBody @Valid productPostRequest: ProductDto.ProductPostRequest
    ): ResponseEntity<ProductDto.ProductResponse> {
        return ResponseEntity.ok().body(productService.addProduct(user, productPostRequest))
    }

    @GetMapping("/{product_id}/")
    @Operation(summary = "개별 판매글 조회", description = "개별 판매글을 조회합니다. 조회수가 1 증가합니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
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
    @Operation(summary = "판매글 수정", description = "판매글의 상세 정보를 수정합니다. 지역정보는 수정할 수 없습니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0202", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "3201", description = "판매자가 아닌 다른 사용자가 수정 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun updateProduct(
        @CurrentUser @ApiIgnore user: User,
        @RequestBody @Valid productPatchRequest: ProductDto.ProductUpdateRequest,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<ProductDto.ProductResponse> {
        return ResponseEntity.ok().body(productService.patchProduct(user, productPatchRequest, productId))
    }

    @PostMapping("/{product_id}/like/")
    @Operation(summary = "판매글 관심목록 등록", description = "판매글을 관심목록에 등록합니다", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "0202", description = "해당 판매글이 이미 판매완료인 경우"),
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

    @PostMapping("/{product_id}/like/cancel/")
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

    @PostMapping("/{product_id}/chat/")
    @Operation(summary = "구매 요청", description = "해당 판매글에 대해 구매 요청을 보냅니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0201", description = "해당 판매글에 이미 구매 요청을 보낸 경우"),
        ApiResponse(responseCode = "0202", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "3208", description = "판매자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun chat(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @RequestBody @Valid purchaseRequest: PurchaseRequestDto.PurchaseRequest
    ): ResponseEntity<PurchaseRequestDto.PurchaseRequestResponse> {
        return ResponseEntity.ok().body(productService.chat(user, productId, purchaseRequest))
    }

    @PostMapping("/{product_id}/reserve/")
    @Operation(summary = "예약중 변경", description = "해당 판매글의 상태를 예약중으로 변경합니다", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "0202", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "3203", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun reserve(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.reserve(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{product_id}/reserve/cancel/")
    @Operation(summary = "예약중 취소", description = "해당 판매글의 상태 예약중을 다시 판매중으로 변경합니다", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "0202", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "3204", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun cancelReserve(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.cancelReserve(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{product_id}/hide/")
    fun hideProduct(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.hideProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{product_id}/show/")
    fun showProduct(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.showProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{product_id}/bump/")
    fun bringUpMyPost(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.bringUpMyPost(user, productId)
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
        ApiResponse(responseCode = "0203", description = "해당 판매글에 대한 구매 요청이 아닌 경우"),
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
    fun chatAgain(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long,
        @RequestBody @Valid purchaseRequest: PurchaseRequestDto.PurchaseRequest
    ): ResponseEntity<PurchaseRequestDto.PurchaseRequestResponse> {
        return ResponseEntity.ok().body(productService.chatAgain(user, productId, purchaseRequestId, purchaseRequest))
    }

    @PostMapping("/{product_id}/purchases/{purchase_request_id}/confirm/")
    @Operation(summary = "구매 확정", description = "해당 판매글에 대한 구매 요청이 확정됩니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0202", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "0203", description = "해당 판매글에 대한 구매 요청이 아닌 경우"),
        ApiResponse(responseCode = "3205", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우"),
        ApiResponse(responseCode = "4201", description = "해당 판매 요청이 없는 경우")
    ])
    fun confirmPurchaseRequest(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): ResponseEntity<PurchaseRequestDto.PurchaseRequestResponse> {
        return ResponseEntity.ok().body(productService.confirmProductPurchaseRequest(user, productId, purchaseRequestId))
    }

    @PostMapping("/{product_id}/purchases/{purchase_request_id}/reject/")
    fun rejectPurchaseRequest(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): ResponseEntity<PurchaseRequestDto.PurchaseRequestResponse> {
        return ResponseEntity.ok().body(productService.rejectProductPurchaseRequest(user, productId, purchaseRequestId))
    }
}