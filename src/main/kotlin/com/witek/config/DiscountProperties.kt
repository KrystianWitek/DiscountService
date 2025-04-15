package com.witek.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "discount")
data class DiscountProperties(
    val adminToken: String
)
