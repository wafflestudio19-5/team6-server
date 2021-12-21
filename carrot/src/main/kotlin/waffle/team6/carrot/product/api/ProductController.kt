package waffle.team6.carrot.product.api

import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.product.dto.ListResponse
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
    // retrieve list of products (optional: by parameters)
    @GetMapping("/")
    fun getProducts(@RequestParam(required = false) title: String?): ResponseEntity<Any> {
        return if (title == null)
            ResponseEntity.ok().body(productService.getProducts())
        else
            ResponseEntity.ok().body(productService.getProductsByTitle(title))
    }

    // post a new product for sale
    @PostMapping("/")
    fun addProducts(
        @CurrentUser user: User,
        @RequestBody @Valid productPostRequest: ProductDto.PostRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.addProducts(user, productPostRequest))
    }

    // retrieve information about specific product
    @GetMapping("/{product_id}/")
    fun getProduct(@PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.getProduct(productId))
    }

    // update information about specific product
    @PutMapping("/{product_id}/")
    fun modifyProduct(
        @CurrentUser user: User,
        @RequestBody @Valid productModifyRequest: ProductDto.ModifyRequest,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.modifyProduct(user, productModifyRequest, productId))
    }

    // delete information about specific product
    @DeleteMapping("/{product_id}/")
    fun deleteProduct(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.deleteProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    // partial update information about specific product
    @PatchMapping("/{product_id}/")
    fun changeProductStatus(
        @CurrentUser user: User,
        @RequestBody @Valid productPatchRequest: ProductDto.PatchRequest,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.patchProduct(user, productPatchRequest, productId))
    }

    // like specific product
    @PostMapping("/{product_id}/like/")
    fun likeProduct(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.likeProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    // redo like specific product
    @PostMapping("/{product_id}/like/cancel/")
    fun cancelLikeProduct(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.cancelLikeProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    // chat for purchase request for specific product
    @PostMapping("/{product_id}/chat/")
    fun chat(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @RequestBody purchaseRequest: PurchaseRequestDto.Request
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.chat(user, productId, purchaseRequest))
    }

    // change the status of the product to RESERVED
    @PostMapping("/{product_id}/reserve/")
    fun reserve(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.reserve(user, productId)
        return ResponseEntity.noContent().build()
    }
    
    // redo the status of the product to FOR_SALE
    @PostMapping("/{product_id}/reserve/cancel/")
    fun cancelReserve(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.cancelReserve(user, productId)
        return ResponseEntity.noContent().build()
    }

    // get purchase requests of specific product
    @GetMapping("/{product_id}/purchases/")
    fun getPurchaseRequests(
        @CurrentUser user: User, @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.getProductPurchaseRequests(user, productId))
    }

    //get specific purchase request of specific product
    @GetMapping("/{product_id}/purchases/{purchase_request_id}/")
    fun getPurchaseRequest(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.getProductPurchaseRequest(user, productId, purchaseRequestId))
    }

    // change the status of the product to SOLD_OUT
    @PostMapping("/{product_id}/purchases/{purchase_request_id}/confirm/")
    fun confirmPurchaseRequest(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(productService.confirmProductPurchaseRequest(user, productId, purchaseRequestId))
    }
}