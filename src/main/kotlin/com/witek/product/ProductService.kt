package com.witek.product

import com.witek.discount.api.DiscountCalculator
import com.witek.product.model.Product
import com.witek.product.model.ProductCalculatePriceResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

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

    fun calculatePrice(productId: UUID, quantity: Long): ProductCalculatePriceResponse {
        val product: Product = productRepository.findByIdOrNull(productId)
            ?: throw NoSuchElementException("Product with id: $productId not found")

        val totalPrice: BigDecimal = discountCalculator.calculate(product.price, quantity)
        return ProductCalculatePriceResponse(totalPrice)
    }

    private fun Product.toResponse(): ProductResponse = ProductResponse(
        id = id,
        name = name,
        price = price,
    )
}
