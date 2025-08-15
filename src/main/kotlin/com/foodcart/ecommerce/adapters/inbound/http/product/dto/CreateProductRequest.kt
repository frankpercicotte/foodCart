package com.foodcart.ecommerce.adapters.inbound.http.product.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class CreateProductRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotBlank(message = "Description is required")
    val description: String,
    @field:DecimalMin("0.00")
    val cost: BigDecimal= BigDecimal.ZERO,
    @field:DecimalMin("0.00")
    val discount: BigDecimal = BigDecimal.ZERO,
    @field:NotBlank(message = "Category Id is required")
    val categoryId: String,
    @field:Min(0)
    val stockQuantity: Int,
    val imageUrl: String? = null,
)
