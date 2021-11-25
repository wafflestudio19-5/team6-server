package waffle.team6.carrot.product.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.product.dto.ListResponse
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.service.ProductService
import waffle.team6.carrot.user.CurrentUser
import waffle.team6.carrot.user.User
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
    ): ResponseEntity<Void> {
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
}