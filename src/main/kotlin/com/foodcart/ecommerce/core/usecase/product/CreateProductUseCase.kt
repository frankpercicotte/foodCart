package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.shared.Result
import java.math.BigDecimal

interface CreateProductUseCase {
    fun execute(input: Input): Result<Product, ProductError>

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