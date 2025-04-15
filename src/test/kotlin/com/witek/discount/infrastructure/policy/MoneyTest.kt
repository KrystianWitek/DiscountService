package com.witek.discount.infrastructure.policy

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class MoneyTest {

    @Test
    fun `should create correct Money class`() {
        // given
        val given = BigDecimal("10.126")

        // when
        val actual = Money.of(given)

        // then
        assertEquals(BigDecimal("10.13"), actual.asBigDecimal())
    }

    @Test
    fun `should subtract values correctly`() {
        // given
        val money1 = Money.of(BigDecimal("100.00"))
        val money2 = Money.of(BigDecimal("25.456"))

        // when
        val result: BigDecimal = money1 - money2

        // then
        assertEquals(BigDecimal("74.54"), result)
    }
}
