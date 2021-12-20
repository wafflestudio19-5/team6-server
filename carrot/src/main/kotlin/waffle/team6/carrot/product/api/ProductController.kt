package waffle.team6.carrot.product.api

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
    @ResponseStatus(HttpStatus.OK)
    fun getProducts(@RequestParam(required = false) title: String?): ListResponse<ProductDto.SimpleResponse> {
        return if (title == null)
            productService.getProducts()
        else
            productService.getProductsByTitle(title)
    }

    // post a new product for sale
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun addProducts(
        @CurrentUser user: User,
        @RequestBody @Valid productPostRequest: ProductDto.PostRequest
    ): ProductDto.Response {
        return productService.addProducts(user, productPostRequest)
    }

    // retrieve information about specific product
    @GetMapping("/{product_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun getProduct(@PathVariable("product_id") productId: Long): ProductDto.Response {
        return productService.getProduct(productId)
    }

    // update information about specific product
    @PutMapping("/{product_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun modifyProduct(
        @CurrentUser user: User,
        @RequestBody @Valid productModifyRequest: ProductDto.ModifyRequest,
        @PathVariable("product_id") productId: Long
    ): ProductDto.Response {
        return productService.modifyProduct(user, productModifyRequest, productId)
    }

    // delete information about specific product
    @DeleteMapping("/{product_id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long
    ): ResponseEntity<Any> {
        productService.deleteProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    // partial update information about specific product
    @PatchMapping("/{product_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun changeProductStatus(
        @CurrentUser user: User,
        @RequestBody @Valid productPatchRequest: ProductDto.PatchRequest,
        @PathVariable("product_id") productId: Long
    ): ProductDto.Response {
        return productService.patchProduct(user, productPatchRequest, productId)
    }

    // like specific product
    @PostMapping("/{product_id}/like/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun likeProduct(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.likeProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    // redo like specific product
    @PostMapping("/{product_id}/like/cancel/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelLikeProduct(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.cancelLikeProduct(user, productId)
        return ResponseEntity.noContent().build()
    }

    // chat for purchase request for specific product
    @PostMapping("/{product_id}/chat/")
    @ResponseStatus(HttpStatus.OK)
    fun chat(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @RequestBody purchaseRequest: PurchaseRequestDto.Request
    ): PurchaseRequestDto.Response {
        return productService.chat(user, productId, purchaseRequest)
    }

    // change the status of the product to RESERVED
    @PostMapping("/{product_id}/reserve/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reserve(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.reserve(user, productId)
        return ResponseEntity.noContent().build()
    }
    
    // redo the status of the product to FOR_SALE
    @PostMapping("/{product_id}/reserve/cancel/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelReserve(@CurrentUser user: User, @PathVariable("product_id") productId: Long): ResponseEntity<Any> {
        productService.cancelReserve(user, productId)
        return ResponseEntity.noContent().build()
    }

    // get purchase requests of specific product
    @GetMapping("/{product_id}/purchases/")
    @ResponseStatus(HttpStatus.OK)
    fun getPurchaseRequests(
        @CurrentUser user: User, @PathVariable("product_id") productId: Long
    ): ListResponse<PurchaseRequestDto.Response> {
        return productService.getProductPurchaseRequests(user, productId)
    }

    //get specific purchase request of specific product
    @GetMapping("/{product_id}/purchases/{purchase_request_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun getPurchaseRequest(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): PurchaseRequestDto.Response {
        return productService.getProductPurchaseRequest(user, productId, purchaseRequestId)
    }

    // change the status of the product to SOLD_OUT
    @PostMapping("/{product_id}/purchases/{purchase_request_id}/confirm/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun confirmPurchaseRequest(
        @CurrentUser user: User,
        @PathVariable("product_id") productId: Long,
        @PathVariable("purchase_request_id") purchaseRequestId: Long
    ): PurchaseRequestDto.Response {
        return productService.confirmProductPurchaseRequest(user, productId, purchaseRequestId)
    }
}