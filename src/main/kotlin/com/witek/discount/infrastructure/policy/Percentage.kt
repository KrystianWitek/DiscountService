package com.witek.discount.infrastructure.policy

import java.math.BigDecimal

@JvmInline
internal value class Percentage private constructor(private val fraction: BigDecimal) {

    companion object {
        fun ofPercent(percent: BigDecimal): Percentage =
            Percentage(percent.divide(BigDecimal(100)))
    }

    fun applyTo(value: BigDecimal): BigDecimal =
        value.multiply(fraction)
}