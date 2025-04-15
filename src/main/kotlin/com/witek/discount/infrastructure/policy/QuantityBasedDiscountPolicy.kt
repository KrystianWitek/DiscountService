package com.witek.discount.infrastructure.policy

import com.witek.discount.domain.DiscountPolicy
import com.witek.discount.domain.DiscountPolicyData
import com.witek.discount.infrastructure.cache.DiscountPolicyCache
import com.witek.discount.infrastructure.db.DiscountPolicyEntity
import com.witek.discount.infrastructure.db.DiscountPolicyType
import java.math.BigDecimal

internal class QuantityBasedDiscountPolicy(
    private val discountPolicyCacheService: DiscountPolicyCache
) : DiscountPolicy {

    override fun calculateDiscount(data: DiscountPolicyData): Money {
        val discountPercentage: Percentage = provideDiscountPercentageByThreshold(data.quantity)
        val totalPrice: BigDecimal = data.price - discountPercentage.applyTo(data.price)
        return Money.of(totalPrice)
    }

    private fun provideDiscountPercentageByThreshold(quantity: Long): Percentage {
        val discountPolicyEntity: DiscountPolicyEntity =
            discountPolicyCacheService.getAll()
                .filter { it.type == DiscountPolicyType.QUANTITY }
                .firstOrNull {
                    val startThreshold: Int = provideThreshold(it.startThresholdLimit)
                    val endThreshold: Int = provideThreshold(it.endThresholdLimit)
                    quantity in startThreshold..endThreshold
                }
                ?: error("Missing discount policy for type: QUANTITY and quantity: $quantity")

        return Percentage.ofPercent(discountPolicyEntity.discountRate)
    }

    private fun provideThreshold(threshold: Int?): Int =
        threshold ?: error("QUANTITY config is invalid. Thresholds are required.")
}