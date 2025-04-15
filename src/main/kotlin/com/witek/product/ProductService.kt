package com.witek.product

import com.witek.discount.api.DiscountCalculator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val discountCalculator: DiscountCalculator
) {

    fun getProductById(id: UUID): ProductResponse {
        val product: Product = productRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Product with id: $id not found")

        return product.toResponse()
    }

    fun calculatePrice(productId: UUID, quantity: Long): BigDecimal {
        val product: Product = productRepository.findByIdOrNull(productId)
            ?: throw NoSuchElementException("Product with id: $productId not found")

        return discountCalculator.calculate(product.price, quantity)
    }

    private fun Product.toResponse(): ProductResponse = ProductResponse(
        id = id.toString(),
        name = name,
        price = price,
    )
}
