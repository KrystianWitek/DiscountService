package com.witek.discount.infrastructure.cache

import com.witek.discount.infrastructure.db.DiscountPolicyEntity

internal interface DiscountPolicyCache {
    
    fun getAll(): List<DiscountPolicyEntity>

    fun refresh()
}