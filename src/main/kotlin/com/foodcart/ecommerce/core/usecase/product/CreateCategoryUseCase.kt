package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.shared.Result
import java.math.BigDecimal

interface CreateCategoryUseCase {
    fun execute(input: Input): Result<Category, ProductError>

    data class Input (
        val categoryId: Long? = null,
        val name: String,
        val profitMargin: BigDecimal,
        val maxDiscount: BigDecimal,
        val isActive: Boolean = true
    )
}
