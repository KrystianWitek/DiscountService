package com.witek.product

import com.witek.product.model.ProductCalculatePriceRequest
import com.witek.product.model.ProductCalculatePriceResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

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

    @PostMapping(
        value = ["/{id}/calculate-price"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Calculation of the final amount with discounts")
    fun calculateProductTotalPrice(
        @PathVariable id: UUID,
        @RequestBody @Valid body: ProductCalculatePriceRequest
    ): ProductCalculatePriceResponse {
        return productService.calculatePrice(id, body.quantity)
    }

}