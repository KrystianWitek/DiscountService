package com.witek.product

import java.math.BigDecimal

data class ProductResponse(
    val id: String,
    val name: String,
    val price: BigDecimal,
)