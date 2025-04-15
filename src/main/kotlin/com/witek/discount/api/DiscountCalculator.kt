package com.witek.discount.api

import java.math.BigDecimal

interface DiscountCalculator {

    fun calculate(price: BigDecimal, quantity: Long): BigDecimal
}