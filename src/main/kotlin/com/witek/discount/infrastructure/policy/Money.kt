package com.witek.discount.infrastructure.policy

import java.math.BigDecimal
import java.math.RoundingMode

@JvmInline
internal value class Money private constructor(private val value: BigDecimal) {

    fun asBigDecimal(): BigDecimal =
        value.setScale(SCALE, MODE)

    operator fun minus(other: Money): BigDecimal {
        return this.value.subtract(other.value).setScale(SCALE, MODE)
    }

    companion object {
        fun of(value: BigDecimal): Money = Money(value.setScale(SCALE, MODE))
        private const val SCALE: Int = 2
        private val MODE = RoundingMode.HALF_UP
    }
}