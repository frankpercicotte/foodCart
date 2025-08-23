package com.foodcart.ecommerce.adapters.inbound.http.product.dto

import java.math.BigDecimal

data class CreateCategoryResponse(
    val categoryId: Long? = null,
    val name: String,
    val profitMargin: BigDecimal,
    val maxDiscount: BigDecimal,
    val isActive: Boolean = true,
)
