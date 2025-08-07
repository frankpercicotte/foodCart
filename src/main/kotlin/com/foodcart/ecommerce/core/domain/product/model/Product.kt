package com.foodcart.ecommerce.core.domain.product.model

import java.math.BigDecimal

data class Product(
    val productId: String? = null,
    val name: String,
    val normalizedName: String,
    val description: String,
    val price: BigDecimal,
    val cost: BigDecimal,
    val discount: BigDecimal = BigDecimal.ZERO,
    val categoryId: String,
    val stockQuantity: Int,
    val isActive: Boolean = false,
    val imageUrl: String? = null,
)
