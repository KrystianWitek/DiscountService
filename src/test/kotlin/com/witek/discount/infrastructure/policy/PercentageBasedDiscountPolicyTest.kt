package com.witek.discount.infrastructure.policy

import com.witek.discount.domain.DiscountPolicyData
import com.witek.discount.infrastructure.cache.DiscountPolicyCache
import com.witek.discount.infrastructure.db.DiscountPolicyEntity
import com.witek.discount.infrastructure.policy.DiscountPolicyEntityCreator.percentageEntity
import com.witek.discount.infrastructure.policy.DiscountPolicyEntityCreator.quantityEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import java.math.BigDecimal
import kotlin.longArrayOf
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PercentageBasedDiscountPolicyTest {
    private val discountPolicyCacheService: DiscountPolicyCache = mock()
    private lateinit var percentageBasedDiscountPolicy: PercentageBasedDiscountPolicy

    @BeforeEach
    fun setUp() {
        reset(discountPolicyCacheService)
        percentageBasedDiscountPolicy = PercentageBasedDiscountPolicy(discountPolicyCacheService)
    }

    @Test
    fun `should throw exception, when there is no percentage policy`() {
        // given
        val data = DiscountPolicyData(quantity = 123, price = BigDecimal(123))
        Mockito.`when`(discountPolicyCacheService.getAll())
            .thenReturn(emptyList())

        // when
        // then
        assertFailsWith<IllegalStateException> {
            percentageBasedDiscountPolicy.calculateDiscount(data)
        }.let {
            assertEquals("There is no PERCENTAGE discount policy", it.message)
        }
    }

    @Test
    fun `should throw exception, when there is more than one percentage policy`() {
        // given
        val data = DiscountPolicyData(quantity = 123, price = BigDecimal(123))
        Mockito.`when`(discountPolicyCacheService.getAll())
            .thenReturn(
                listOf(
                    percentageEntity(startThresholdLimit = null, endThresholdLimit = null, discountRate = 13),
                    percentageEntity(startThresholdLimit = null, endThresholdLimit = null, discountRate = 55)
                )
            )

        // when
        // then
        assertFailsWith<IllegalStateException> {
            percentageBasedDiscountPolicy.calculateDiscount(data)
        }.let {
            assertEquals("There is too much PERCENTAGE discount policies", it.message)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "123,",
        ",345",
        "123,345",
    )
    fun `should throw exception, when there is percentage policy with threshold`(start: Int?, end: Int?) {
        // given
        val data = DiscountPolicyData(quantity = 123, price = BigDecimal(123))
        Mockito.`when`(discountPolicyCacheService.getAll())
            .thenReturn(
                listOf(
                    percentageEntity(startThresholdLimit = start, endThresholdLimit = end, discountRate = 13)
                )
            )

        // when
        // then
        assertFailsWith<IllegalStateException> {
            percentageBasedDiscountPolicy.calculateDiscount(data)
        }.let {
            assertEquals("PERCENTAGE discount policy not require any of thresholds", it.message)
        }
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = ',',
        value = [
            "1000, 800.00",
            "555, 444.00",
            "123, 98.40",
        ]
    )
    fun `should calculate total price depending on policies`(
        baseProductPrice: BigDecimal,
        expectedTotalPrice: BigDecimal
    ) {
        // given
        val data = DiscountPolicyData(quantity = 123, price = baseProductPrice)
        Mockito.`when`(discountPolicyCacheService.getAll())
            .thenReturn(validDiscountPolicyEntities)

        // when
        val result: Money = percentageBasedDiscountPolicy.calculateDiscount(data)

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