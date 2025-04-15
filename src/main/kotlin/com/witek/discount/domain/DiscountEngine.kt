package com.witek.discount.domain

import com.witek.discount.infrastructure.policy.Money
import java.math.BigDecimal

internal class DiscountEngine(
    private val discountPolicies: List<DiscountPolicy>,
) {

    fun provideLowestPrice(quantity: Long, price: BigDecimal): Money {
        val discountData = DiscountPolicyData(quantity, price)
        return discountPolicies
            .map { it.calculateDiscount(discountData) }
            .minByOrNull { it.asBigDecimal() }
            ?: Money.of(price)
    }
}