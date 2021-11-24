package waffle.team6.carrot.product.service

import org.springframework.stereotype.Service
import waffle.team6.carrot.product.dto.ListResponse
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.product.repository.ProductRepository

@Service
class ProductService (
    private val productRepository: ProductRepository,
    // userRepository: ProductRepository
){
    fun getProducts(): ListResponse<ProductDto.SimpleResponse> {
        return ListResponse(productRepository.findAll().map { ProductDto.SimpleResponse(it) })
    }

    fun getProductsByTitle(title: String): ListResponse<ProductDto.SimpleResponse> {
        return ListResponse(productRepository.findAllContainingTitle(title).map { ProductDto.SimpleResponse(it) })
    }

    fun addProducts() {

    }

    fun getProduct() {

    }

    fun modifyProduct() {

    }

    fun deleteProduct() {

    }

    fun changeProductStatus() {

    }
}