package waffle.team6.carrot.product.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.product.dto.ListResponse
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.service.ProductService

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

    // TODO: POST "/"
    // post a new product for sale
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun addProducts() {

    }

    // TODO: GET "/{product_id}/"
    // retrieve information about specific product
    @GetMapping("/{product_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun getProduct() {

    }

    // TODO: PUT "/{product_id}/"
    // update information about specific product
    @PostMapping("/{product_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun modifyProduct() {

    }

    // TODO: DELETE "/{product_id}/"
    // delete information about specific product
    @DeleteMapping("/{product_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun deleteProduct() {

    }

    // TODO: PATCH "/{product_id}/"
    // reserve a deal or confirm purchase
    @PatchMapping("/{product_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun changeProductStatus() {

    }
}