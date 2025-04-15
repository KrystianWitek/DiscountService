package com.witek.discount.infrastructure.policy

import com.witek.discount.domain.DiscountPolicyData
import com.witek.discount.infrastructure.cache.DiscountPolicyCache
import com.witek.discount.infrastructure.db.DiscountPolicyEntity
import com.witek.discount.infrastructure.policy.DiscountPolicyEntityCreator.percentageEntity
import com.witek.discount.infrastructure.policy.DiscountPolicyEntityCreator.quantityEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class QuantityBasedDiscountPolicyTest {
    private val discountPolicyCacheService: DiscountPolicyCache = mock()
    private lateinit var quantityBasedDiscountPolicy: QuantityBasedDiscountPolicy

    @BeforeEach
    fun setUp() {
        reset(discountPolicyCacheService)
        quantityBasedDiscountPolicy = QuantityBasedDiscountPolicy(discountPolicyCacheService)
    }

    @Test
    fun `should throw exception, when any of thresholds is null`() {
        // given
        val data = DiscountPolicyData(quantity = 123, price = BigDecimal(123))
        Mockito.`when`(discountPolicyCacheService.getAll())
            .thenReturn(listOf(quantityEntity(startThresholdLimit = null, endThresholdLimit = 34, discountRate = 0)))

        // when
        // then
        assertFailsWith<IllegalStateException> {
            quantityBasedDiscountPolicy.calculateDiscount(data)
        }.let {
            assertEquals("QUANTITY config is invalid. Thresholds are required.", it.message)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [123456789, -3])
    fun `should throw exception, when there is no config for quantity value`(quantityNotMatchingToConfig: Long) {
        // given
        val data = DiscountPolicyData(quantity = quantityNotMatchingToConfig, price = BigDecimal(123))
        Mockito.`when`(discountPolicyCacheService.getAll())
            .thenReturn(validDiscountPolicyEntities)

        // when
        // then
        assertFailsWith<IllegalStateException> {
            quantityBasedDiscountPolicy.calculateDiscount(data)
        }.let {
            assertEquals(
                expected = "Missing discount policy for type: QUANTITY and quantity: $quantityNotMatchingToConfig",
                actual = it.message
            )
        }
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = ',',
        value = [
            "1000, 1, 1000.00",
            "1000, 9, 1000.00",
            "1000, 10, 950.00",
            "1000, 19, 950.00",
            "1000, 20, 900.00",
            "1000, 49, 900.00",
            "1000, 50, 850.00",
            "1000, 889, 850.00",
        ]
    )
    fun `should calculate total price depending on policies`(
        baseProductPrice: BigDecimal,
        quantity: Long,
        expectedTotalPrice: BigDecimal
    ) {
        // given
        val data = DiscountPolicyData(quantity = quantity, price = baseProductPrice)
        Mockito.`when`(discountPolicyCacheService.getAll())
            .thenReturn(validDiscountPolicyEntities)

        // when
        val result: Money = quantityBasedDiscountPolicy.calculateDiscount(data)

        // then
        assertThat(result.asBigDecimal()).isEqualByComparingTo(expectedTotalPrice)
    }

    private val validDiscountPolicyEntities: List<DiscountPolicyEntity> by lazy {
        listOf(
            quantityEntity(startThresholdLimit = 1, endThresholdLimit = 9, discountRate = 0),
            quantityEntity(startThresholdLimit = 10, endThresholdLimit = 19, discountRate = 5),
            quantityEntity(startThresholdLimit = 20, endThresholdLimit = 49, discountRate = 10),
            quantityEntity(startThresholdLimit = 50, endThresholdLimit = 999, discountRate = 15),
            percentageEntity(startThresholdLimit = null, endThresholdLimit = null, discountRate = 20)
        )
    }
}