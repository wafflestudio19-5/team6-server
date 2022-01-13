package waffle.team6.carrot.purchaseOrders.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import waffle.team6.carrot.purchaseOrders.dto.PurchaseOrderDto
import waffle.team6.carrot.purchaseOrders.service.PurchaseOrderService
import waffle.team6.carrot.user.model.User
import waffle.team6.global.auth.CurrentUser
import javax.validation.Valid
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@Validated
@RestController
@RequestMapping("/api/v1/purchase-orders")
class PurchaseOrderController(
    private val purchaseOrderService: PurchaseOrderService
) {
    @GetMapping("/")
    @Operation(summary = "구매 요청 조회", description = "해당 판매글에 대한 모든 구매 요청이 조회됩니다. 가격 제안이 있는 구매 요청을 따로 볼 수 있습니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "3205", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우")
    ])
    fun getPurchaseRequests(
        @CurrentUser @ApiIgnore user: User,
        @RequestParam(required = true) @Positive productId: Long,
        @RequestParam(required = true) @PositiveOrZero pageNumber: Int,
        @RequestParam(required = true) @Positive pageSize: Int,
        @RequestParam(required = false) pendingOnly: Boolean? = false,
        @RequestParam(required = false) withPriceSuggestion: Boolean? = false
    ): ResponseEntity<Page<PurchaseOrderDto.PurchaseOrderResponse>> {
        return if (pendingOnly == true) ResponseEntity.ok().body(purchaseOrderService
            .getAcceptedProductPurchaseRequests(user, productId, pageNumber, pageSize))
        else if (withPriceSuggestion == true) ResponseEntity.ok().body(purchaseOrderService
            .getProductPurchaseRequestsWithPriceSuggestion(user, productId, pageNumber, pageSize))
        else ResponseEntity.ok().body(purchaseOrderService
            .getProductPurchaseRequests(user, productId, pageNumber, pageSize))
    }

    @PostMapping("/")
    @Operation(summary = "구매 요청", description = "해당 판매글에 대해 구매 요청을 보냅니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0201", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "3208", description = "판매자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우"),
        ApiResponse(responseCode = "9202", description = "해당 판매글에 이미 구매 요청을 보낸 경우")
    ])
    fun chat(
        @CurrentUser @ApiIgnore user: User,
        @RequestBody @Valid purchaseRequest: PurchaseOrderDto.PurchaseOrderPostRequest
    ): ResponseEntity<PurchaseOrderDto.PurchaseOrderResponse> {
        return ResponseEntity.ok().body(purchaseOrderService.chat(user, purchaseRequest))
    }

    @GetMapping("/{purchase_request_id}/")
    @Operation(summary = "개별 구매 요청 조회", description = "해당 판매글에 대한 개별 구매 요청이 조회됩니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0202", description = "해당 판매글에 대한 구매 요청이 아닌 경우"),
        ApiResponse(responseCode = "3205", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4201", description = "해당 구매 요청이 없는 경우")
    ])
    fun getPurchaseRequest(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("purchase_request_id") purchaseRequestId: Long,
    ): ResponseEntity<PurchaseOrderDto.PurchaseOrderResponse> {
        return ResponseEntity.ok().body(purchaseOrderService.getProductPurchaseRequest(user, purchaseRequestId))
    }

    @PutMapping("/{purchase_request_id}/")
    @Operation(summary = "구매 요청 다시 보내기", description = "해당 구매 요청을 바꿔서 다시 보냅니다", responses = [
        ApiResponse(responseCode = "200", description = "Success Response"),
        ApiResponse(responseCode = "0201", description = "해당 판매글이 이미 판매완료인 경우"),
        ApiResponse(responseCode = "0202", description = "해당 판매글에 대한 구매 요청이 아닌 경우"),
        ApiResponse(responseCode = "0205", description = "해당 판매요청이 이미 수락된 경우"),
        ApiResponse(responseCode = "3213", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우"),
        ApiResponse(responseCode = "4201", description = "해당 구매 요청이 없는 경우")
    ])
    fun chatAgain(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("purchase_request_id") purchaseRequestId: Long,
        @RequestBody @Valid purchaseRequest: PurchaseOrderDto.PurchaseOrderUpdateRequest
    ): ResponseEntity<PurchaseOrderDto.PurchaseOrderResponse> {
        return ResponseEntity.ok().body(purchaseOrderService.chatAgain(user, purchaseRequestId, purchaseRequest))
    }

    @DeleteMapping("/{purchase_request_id}/")
    @Operation(summary = "구매 요청 취소", description = "해당 구매 요청을 취소합니다", responses = [
        ApiResponse(responseCode = "204", description = "Success Response"),
        ApiResponse(responseCode = "0205", description = "해당 판매요청이 이미 수락된 경우"),
        ApiResponse(responseCode = "0202", description = "해당 판매글에 대한 구매 요청이 아닌 경우"),
        ApiResponse(responseCode = "3204", description = "판매자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4200", description = "해당 판매글이 없는 경우"),
        ApiResponse(responseCode = "4201", description = "해당 구매 요청이 없는 경우")
    ])
    fun deleteChat(
        @CurrentUser @ApiIgnore user: User,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): ResponseEntity<Any> {
        purchaseOrderService.deleteChat(user, purchaseRequestId)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{purchase_request_id}/status/")
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
        @PathVariable("purchase_request_id") purchaseRequestId: Long,
        @RequestBody @Valid handleRequest: PurchaseOrderDto.PurchaseOrderHandleRequest
    ): ResponseEntity<PurchaseOrderDto.PurchaseOrderResponse> {
        return ResponseEntity.ok().body(purchaseOrderService.handleProductOrder(user, purchaseRequestId, handleRequest))
    }
}