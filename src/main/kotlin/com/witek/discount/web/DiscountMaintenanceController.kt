package com.witek.discount.web

import com.witek.ErrorResponse
import com.witek.config.DiscountProperties
import com.witek.discount.infrastructure.cache.DiscountPolicyCache
import com.witek.discount.web.CustomHeaders.ADMIN_TOKEN_HEADER
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/maintenance/cache")
@Hidden
internal class DiscountMaintenanceController(
    private val cache: DiscountPolicyCache,
    private val properties: DiscountProperties
) {

    @PostMapping("/refresh")
    fun refresh(@RequestHeader(ADMIN_TOKEN_HEADER) token: String?): ResponseEntity<Any> {
        return if (token == properties.adminToken) {
            cache.refresh()
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(UNAUTHORIZED))
        }
    }

    private companion object {
        const val UNAUTHORIZED = "Unauthorized"
    }
}