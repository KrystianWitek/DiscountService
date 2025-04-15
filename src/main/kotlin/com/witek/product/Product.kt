package com.witek.product

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("PRODUCTS")
data class Product(
    @Id val id: String,
    val name: String,
    val price: BigDecimal,
)