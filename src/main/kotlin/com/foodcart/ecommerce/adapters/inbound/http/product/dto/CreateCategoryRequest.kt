package com.foodcart.ecommerce.adapters.inbound.http.product.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class CreateCategoryRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:DecimalMin("0.00")
    val profitMargin: BigDecimal= BigDecimal.ZERO,
    @field:DecimalMin("0.00")
    val maxDiscount: BigDecimal = BigDecimal.ZERO,
    val isActive: Boolean = true,
)
