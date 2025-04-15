package com.witek.discount.domain

import com.witek.discount.infrastructure.policy.Money
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DiscountEngineTest {
    private lateinit var discountEngine: DiscountEngine

    @Test
    fun `should return base price, when there are no discount policies available`() {
        // given
        val discountPolicies: List<DiscountPolicy> = emptyList()
        discountEngine = DiscountEngine(discountPolicies)

        // when
        val lowestPrice: Money = discountEngine.provideLowestPrice(QUANTITY, BASE_PRICE)

        // then
        assertThat(lowestPrice.asBigDecimal()).isEqualByComparingTo(BASE_PRICE)
    }

    @Test
    fun `should return lowest price from calculated discount policies`() {
        // given
        val expectedLowestDiscount = BigDecimal("13")
        val discountPolicies: List<DiscountPolicy> = listOf(
            createDiscountPolicy(calculatedValue = BigDecimal("22")),
            createDiscountPolicy(calculatedValue = BigDecimal("45")),
            createDiscountPolicy(calculatedValue = expectedLowestDiscount),
            createDiscountPolicy(calculatedValue = BigDecimal("10000"))
        )
        discountEngine = DiscountEngine(discountPolicies)

        // when
        val lowestPrice: Money = discountEngine.provideLowestPrice(QUANTITY, BASE_PRICE)

        // then
        assertThat(lowestPrice.asBigDecimal()).isEqualByComparingTo(expectedLowestDiscount)
    }

    private fun createDiscountPolicy(calculatedValue: BigDecimal): DiscountPolicy =
        object : DiscountPolicy {
            override fun calculateDiscount(data: DiscountPolicyData): Money {
                return Money.of(calculatedValue)
            }
        }

    private companion object {
        const val QUANTITY: Long = 123
        val BASE_PRICE = BigDecimal(800)
    }
}