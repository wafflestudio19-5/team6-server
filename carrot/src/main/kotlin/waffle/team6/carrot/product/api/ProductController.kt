package waffle.team6.carrot.product.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import waffle.team6.carrot.product.service.ProductService

@RestController
@RequestMapping("/api/v1/products")
class ProductController (
    private val productService: ProductService
    ) {
    // TODO: GET "/"
    // retrieve list of products (optional: by parameters)
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getProducts() {

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