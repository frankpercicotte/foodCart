package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.exception.CategoryAlreadyExistsException
import com.foodcart.ecommerce.core.domain.product.model.Category
import java.math.BigDecimal

interface CreateCategoryUseCase {
    @Throws(CategoryAlreadyExistsException::class)
    fun execute(input: Input): Category

    data class Input (
        val categoryId: Long? = null,
        val name: String,
        val profitMargin: BigDecimal,
        val maxDiscount: BigDecimal,
        val isActive: Boolean = true
    )
}
