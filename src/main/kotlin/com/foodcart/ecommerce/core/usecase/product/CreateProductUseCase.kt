package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.product.model.Product
import java.math.BigDecimal

interface CreateProductUseCase {
    fun execute(input: Input): Product

    data class Input (
        val productId: Long? = null,
        val name: String,
        val description: String,
        val cost: BigDecimal,
        val discount: BigDecimal = BigDecimal.ZERO,
        val categoryId: Long,
        val stockQuantity: Int,
        val isActive: Boolean = true,
        val imageUrl: String? = null,
    )
}