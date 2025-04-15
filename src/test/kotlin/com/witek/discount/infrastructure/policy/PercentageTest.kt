package com.witek.discount.infrastructure.policy

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.test.assertEquals

class PercentageTest {

    @ParameterizedTest
    @CsvSource(
        "400, 20, 80.00",
        "366.45, 18, 65.96",
        "34.33, 13, 4.46",
    )
    fun `should calculate price percentage part`(price: BigDecimal, percentageValue: BigDecimal, expected: BigDecimal) {
        // given
        val percentage = Percentage.ofPercent(percentageValue)

        // when
        val actual: BigDecimal = percentage.applyTo(price).setScale(2, RoundingMode.HALF_UP)

        // then
        assertEquals(expected, actual)
    }
}