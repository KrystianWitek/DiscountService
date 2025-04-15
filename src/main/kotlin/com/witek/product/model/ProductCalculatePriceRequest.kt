package com.witek.product.model

import jakarta.validation.constraints.Positive

data class ProductCalculatePriceRequest(
    @field:Positive val quantity: Long
)