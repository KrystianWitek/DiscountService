package com.witek.discount.domain

import com.witek.discount.infrastructure.policy.Money

internal interface DiscountPolicy {

    fun calculateDiscount(data: DiscountPolicyData): Money
}

