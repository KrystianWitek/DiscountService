package com.witek.discount.infrastructure.policy

import com.witek.discount.infrastructure.db.DiscountPolicyEntity
import com.witek.discount.infrastructure.db.DiscountPolicyType
import java.math.BigDecimal
import java.util.UUID

internal object DiscountPolicyEntityCreator {

    fun quantityEntity(
        startThresholdLimit: Int?,
        endThresholdLimit: Int?,
        discountRate: Int
    ): DiscountPolicyEntity {
        return createDiscountPolicyEntity(
            startThresholdLimit = startThresholdLimit,
            endThresholdLimit = endThresholdLimit,
            discountRate = discountRate,
            type = DiscountPolicyType.QUANTITY
        )
    }

    fun percentageEntity(
        startThresholdLimit: Int?,
        endThresholdLimit: Int?,
        discountRate: Int
    ): DiscountPolicyEntity {
        return createDiscountPolicyEntity(
            startThresholdLimit = startThresholdLimit,
            endThresholdLimit = endThresholdLimit,
            discountRate = discountRate,
            type = DiscountPolicyType.PERCENTAGE
        )
    }

    private fun createDiscountPolicyEntity(
        startThresholdLimit: Int?,
        endThresholdLimit: Int?,
        discountRate: Int,
        type: DiscountPolicyType
    ): DiscountPolicyEntity {
        return DiscountPolicyEntity(
            id = UUID.randomUUID(),
            startThresholdLimit = startThresholdLimit,
            endThresholdLimit = endThresholdLimit,
            discountRate = BigDecimal(discountRate),
            type = type
        )
    }
}