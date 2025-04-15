package com.witek.product

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.witek.discount.infrastructure.db.DiscountPolicyRepository
import com.witek.discount.web.DiscountMaintenanceController
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.math.BigDecimal
import java.util.UUID

@WebMvcTest(controllers = [ProductController::class])
@ActiveProfiles("test")
@MockitoBean(types = [DiscountPolicyRepository::class, ProductRepository::class])
internal class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var productService: ProductService

    @Test
    fun `should read product details`() {
        val response = ProductResponse(
            id = PRODUCT_ID.toString(),
            name = "Rosanne Gregory",
            price = BigDecimal(132)
        )
        Mockito.`when`(productService.getProductById(PRODUCT_ID))
            .thenReturn(response)

        mockMvc.get("/v1/products/$PRODUCT_ID")
            .andExpect {
                status { isOk() }
            }

        verify(productService).getProductById(PRODUCT_ID)
        verifyNoMoreInteractions(productService)
    }

    @Test
    fun `should calculate product total price`() {
        val request = ProductCalculatePriceRequest(quantity = QUANTITY)

        mockMvc.post("/v1/products/$PRODUCT_ID/calculate-price") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(request)
        }.andExpect {
            status { isOk() }
        }

        verify(productService).calculatePrice(PRODUCT_ID, QUANTITY)
        verifyNoMoreInteractions(productService)
    }

    private companion object {
        val PRODUCT_ID: UUID = UUID.fromString("2404ead6-4d4e-4b17-8ebf-7fccaba34f73")
        const val QUANTITY: Long = 123
    }
}