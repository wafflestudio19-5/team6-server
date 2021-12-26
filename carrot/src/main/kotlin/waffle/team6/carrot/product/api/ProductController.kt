package waffle.team6.carrot.product.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.dto.PurchaseRequestDto
import waffle.team6.carrot.product.service.ProductService
import waffle.team6.global.auth.CurrentUser
import waffle.team6.carrot.user.model.User
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/products")
class ProductController (
    private val productService: ProductService
    ) {
    @GetMapping("/")
    fun getProducts(@RequestParam(required = false) title: String?): ResponseEntity<Any> {
        return if (title == null)
            ResponseEntity.ok().body(productService.getProducts())
        else
            ResponseEntity.ok().body(productService.getProductsByTitle(title))
    }

    @PostMapping("/")
    fun addProducts(
        @CurrentUser user: User,
        @RequestBody @Valid productPostRequest: ProductDto.PostRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.addProducts(user, productPostRequest))
    }

    @GetMapping("/{product_id}/")
    fun getProduct(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.getProduct(user, productId))
    }

    @DeleteMapping("/{product_id}/")
    fun deleteProduct(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.deleteProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{product_id}/")
    fun changeProductStatus(
        @CurrentUser user: User,
        @RequestBody @Valid productPatchRequest: ProductDto.PatchRequest,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.patchProduct(user, productPatchRequest, productId))
    }

    @PostMapping("/{product_id}/like/")
    fun likeProduct(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.likeProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{product_id}/like/cancel/")
    fun cancelLikeProduct(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.cancelLikeProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{product_id}/chat/")
    fun chat(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @RequestBody purchaseRequest: PurchaseRequestDto.Request
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.chat(user, productId, purchaseRequest))
    }

    @PostMapping("/{product_id}/reserve/")
    fun reserve(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.reserve(user, productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{product_id}/reserve/cancel/")
    fun cancelReserve(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.cancelReserve(user, productId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{product_id}/purchases/")
    fun getPurchaseRequests(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @RequestParam(required = false) withPriceSuggestion: Boolean
    ): ResponseEntity<Any> {
        return if (withPriceSuggestion) ResponseEntity.ok().body(productService
            .getProductPurchaseRequestsWithPriceSuggestion(user, productId))
        else ResponseEntity.ok().body(productService.getProductPurchaseRequests(user, productId))
    }

    @GetMapping("/{product_id}/purchases/{purchase_request_id}/")
    fun getPurchaseRequest(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.getProductPurchaseRequest(user, productId, purchaseRequestId))
    }

    @PostMapping("/{product_id}/purchases/{purchase_request_id}/confirm/")
    fun confirmPurchaseRequest(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.confirmProductPurchaseRequest(user, productId, purchaseRequestId))
    }
}