package com.witek.discount.domain

import java.math.BigDecimal

internal data class DiscountPolicyData(
    val quantity: Long,
    val price: BigDecimal,
)