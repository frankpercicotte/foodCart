package com.foodcart.ecommerce.adapters.inbound.http.product.dto

import java.math.BigDecimal

data class CreateProductResponse(
    val productId: String? = null,
    val name: String,
    val price: BigDecimal,
    val description: String,
    val cost: BigDecimal,
    val discount: BigDecimal,
    val categoryId: String,
    val stockQuantity: Int,
    val isActive: Boolean = true,
    val imageUrl: String? = null,
)
