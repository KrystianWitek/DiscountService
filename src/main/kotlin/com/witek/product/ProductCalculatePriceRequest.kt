package com.witek.product

import jakarta.validation.constraints.Positive

data class ProductCalculatePriceRequest(
    @field:Positive val quantity: Long
)