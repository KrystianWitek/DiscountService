package com.witek.discount.application

import com.witek.discount.api.DiscountCalculator
import com.witek.discount.domain.DiscountEngine
import java.math.BigDecimal

internal class DefaultDiscountCalculator(
    private val engine: DiscountEngine
) : DiscountCalculator {

    override fun calculate(price: BigDecimal, quantity: Long): BigDecimal {
        return engine.provideLowestPrice(quantity, price).asBigDecimal()
    }
}