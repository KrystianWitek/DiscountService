package com.witek.product

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/v1/products")
@Validated
@Tag(name = "Product", description = "Product API")
internal class ProductController(
    private val productService: ProductService,
) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Read single product details by id")
    fun readProductDetails(@PathVariable id: UUID): ProductResponse {
        return productService.getProductById(id)
    }

    @PostMapping("/{id}/calculate-price")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Calculation of the final amount with discounts")
    fun calculateProductTotalPrice(
        @PathVariable id: UUID,
        @RequestBody @Valid body: ProductCalculatePriceRequest
    ): BigDecimal {
        return productService.calculatePrice(id, body.quantity)
    }

}