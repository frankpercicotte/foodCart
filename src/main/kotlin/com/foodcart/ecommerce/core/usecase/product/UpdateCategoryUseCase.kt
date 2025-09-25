package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.common.exception.CategoryNotFoundException
import com.foodcart.ecommerce.core.domain.common.exception.DatabaseOperationException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidCategoryNameException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidMaxDiscountException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidProfitMarginException
import com.foodcart.ecommerce.core.domain.common.exception.MaxDiscountExceededException
import java.math.BigDecimal


class UpdateCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    fun execute(request: UpdateCategoryRequest): Category {
        val existingCategory = categoryRepository.findById(request.id)
            ?: throw CategoryNotFoundException(request.id)

        validateRequestOrThrow(request)

        if (request.name != existingCategory.name &&
            categoryRepository.existsByName(request.name)) {
            throw InvalidCategoryNameException(request.name)
        }

        val updatedCategory = Category(
            categoryId = existingCategory.categoryId,
            name = request.name,
            profitMargin = request.profitMargin,
            maxDiscount = request.maxDiscount,
            isActive = request.isActive
        )

        return try {
            categoryRepository.update(updatedCategory)
        } catch (e: Exception) {
            throw DatabaseOperationException("update(category)", "Failed to update category", e)
        }
    }

    private fun validateRequestOrThrow(request: UpdateCategoryRequest) {
        if (request.name.isBlank()) {
            throw InvalidCategoryNameException(request.name)
        }

        if (request.profitMargin < BigDecimal.ZERO) {
            throw InvalidProfitMarginException(request.profitMargin)
        }

        if (request.maxDiscount < BigDecimal.ZERO) {
            throw InvalidMaxDiscountException(request.maxDiscount)
        }

        if (request.maxDiscount > BigDecimal(100)) {
            throw MaxDiscountExceededException(request.maxDiscount)
        }
    }
}


data class UpdateCategoryRequest(
    val id: Long,
    val name: String,
    val profitMargin: BigDecimal,
    val maxDiscount: BigDecimal,
    val isActive: Boolean
)
