package com.witek.discount

import com.witek.config.WithMySqlTestContainer
import com.witek.discount.api.DiscountCalculator
import com.witek.discount.infrastructure.cache.DiscountPolicyCache
import com.witek.discount.infrastructure.db.DiscountPolicyRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.springframework.test.context.jdbc.Sql
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/schema.sql")
@ActiveProfiles("test")
class DiscountIntegrationTest : WithMySqlTestContainer {

    @Autowired
    private lateinit var calculator: DiscountCalculator

    @MockitoSpyBean
    private lateinit var discountPolicyCache: DiscountPolicyCache

    @MockitoSpyBean
    private lateinit var discountPolicyRepository: DiscountPolicyRepository

    @BeforeEach
    fun setUp() {
        discountPolicyCache.refresh()
        verify(discountPolicyCache).refresh()
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = ',',
        value = [
            "1000, 1, 960.00",
            "1000, 9, 960.00",
            "1000, 10, 950.00",
            "1000, 19, 950.00",
            "1000, 20, 900.00",
            "1000, 49, 900.00",
            "1000, 50, 850.00",
            "1000, 1233, 850.00",
        ]
    )
    fun `should calculate total price depending on DB configuration from sql file`(
        baseProductPrice: BigDecimal,
        quantity: Long,
        expectedTotalPrice: BigDecimal
    ) {
        val result: BigDecimal = calculator.calculate(baseProductPrice, quantity)
        assertEquals(expectedTotalPrice, result)

        verify(discountPolicyRepository).findAll()
        verify(discountPolicyCache).getAll()
        verifyNoMoreInteractions(discountPolicyCache, discountPolicyRepository)
    }
}