package com.witek.product

import com.witek.discount.api.DiscountCalculator
import com.witek.product.model.Product
import com.witek.product.model.ProductCalculatePriceResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertFailsWith

class ProductServiceTest {
    private val productRepository: ProductRepository = mock()
    private val discountCalculator: DiscountCalculator = mock()
    private lateinit var service: ProductService

    @BeforeEach
    fun setUp() {
        reset(productRepository, discountCalculator)
        service = ProductService(productRepository, discountCalculator)
    }

    @Test
    fun `should throw exception, when product by ID is not found`() {
        assertFailsWith<NoSuchElementException> {
            service.getProductById(PRODUCT_ID)
        }.let {
            assertEquals("Product with id: 2404ead6-4d4e-4b17-8ebf-7fccaba34f73 not found", it.message)
        }
    }

    @Test
    fun `should return product details`() {
        // given
        val expectedProduct = Product(
            id = PRODUCT_ID.toString(),
            name = "myProductName",
            price = BigDecimal("123")
        )
        Mockito.`when`(productRepository.findById(PRODUCT_ID))
            .thenReturn(Optional.of(expectedProduct))

        // when
        val response: ProductResponse = service.getProductById(PRODUCT_ID)

        // then
        assertEquals(expectedProduct.id, response.id)
        assertEquals(expectedProduct.price, response.price)
        assertEquals(expectedProduct.name, response.name)
    }

    @Test
    fun `should throw exception, when product by ID is not found during price calculation`() {
        assertFailsWith<NoSuchElementException> {
            service.calculatePrice(PRODUCT_ID, QUANTITY)
        }.let {
            assertEquals("Product with id: 2404ead6-4d4e-4b17-8ebf-7fccaba34f73 not found", it.message)
        }
    }

    @Test
    fun `should return total price with discount`() {
        // given
        val expectedProduct = Product(
            id = PRODUCT_ID.toString(),
            name = "myProductName",
            price = BigDecimal("123")
        )
        val expectedTotalPrice = BigDecimal("55")
        Mockito.`when`(productRepository.findById(PRODUCT_ID))
            .thenReturn(Optional.of(expectedProduct))
        Mockito.`when`(discountCalculator.calculate(expectedProduct.price, QUANTITY))
            .thenReturn(expectedTotalPrice)

        // when
        val response: ProductCalculatePriceResponse = service.calculatePrice(PRODUCT_ID, QUANTITY)

        // then
        assertEquals(expectedTotalPrice, response.totalPrice)
    }

    private companion object {
        val PRODUCT_ID: UUID = UUID.fromString("2404ead6-4d4e-4b17-8ebf-7fccaba34f73")
        const val QUANTITY: Long = 123
    }
}