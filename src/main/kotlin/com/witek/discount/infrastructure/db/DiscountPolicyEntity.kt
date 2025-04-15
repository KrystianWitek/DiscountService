package com.witek.discount.infrastructure.db

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.util.UUID

@Table("DISCOUNT_POLICY_CONFIGS")
internal data class DiscountPolicyEntity(
    @Id val id: UUID,
    val startThresholdLimit: Int?,
    val endThresholdLimit: Int?,
    val discountRate: BigDecimal,
    val type: DiscountPolicyType
)