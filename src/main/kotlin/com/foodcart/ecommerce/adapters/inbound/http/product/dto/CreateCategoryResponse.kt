package com.foodcart.ecommerce.adapters.inbound.http.product.dto

import java.math.BigDecimal

data class CreateCategoryResponse(
    val categoryId: String,
    val name: String,
    val profitMargin: BigDecimal,
    val maxDiscount: BigDecimal,
)
