package com.witek.discount.infrastructure.policy

import com.witek.discount.domain.DiscountPolicy
import com.witek.discount.domain.DiscountPolicyData
import com.witek.discount.infrastructure.cache.DiscountPolicyCache
import com.witek.discount.infrastructure.db.DiscountPolicyEntity
import com.witek.discount.infrastructure.db.DiscountPolicyType
import java.math.BigDecimal
import java.math.RoundingMode

internal class PercentageBasedDiscountPolicy(
    private val discountPolicyCacheService: DiscountPolicyCache
) : DiscountPolicy {

    override fun calculateDiscount(data: DiscountPolicyData): Money {
        val discountPercentage: Percentage = provideDiscountPercentageByThreshold()
        val totalPrice: BigDecimal = data.price - discountPercentage.applyTo(data.price)
        return Money.of(totalPrice)
    }

    private fun provideDiscountPercentageByThreshold(): Percentage {
        val discountPolicyEntity: DiscountPolicyEntity = discountPolicyCacheService
            .getAll()
            .filter { it.type == DiscountPolicyType.PERCENTAGE }
            .validate()
            .single()

        return Percentage.ofPercent(discountPolicyEntity.discountRate)
    }

    private fun List<DiscountPolicyEntity>.validate(): List<DiscountPolicyEntity> {
        if (isEmpty()) error("There is no PERCENTAGE discount policy")
        if (size > 1) error("There is too much PERCENTAGE discount policies")
        if (any { it.startThresholdLimit != null || it.endThresholdLimit != null })
            error("PERCENTAGE discount policy not require any of thresholds")
        return this
    }
}