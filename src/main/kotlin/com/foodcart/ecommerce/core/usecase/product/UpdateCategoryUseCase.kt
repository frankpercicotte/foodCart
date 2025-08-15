package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.shared.Result
import java.math.BigDecimal


class UpdateCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    fun execute(request: UpdateCategoryRequest): Result<Category, ProductError> {
        val existingCategory = categoryRepository.findById(request.id)
            ?: return Result.Failure(ProductError.CategoryNotFound(request.id))

        val validationResult = validateRequest(request)
        if (validationResult.isFailure()) {
            return validationResult as Result.Failure<Category, ProductError>
        }

        if (request.name != existingCategory.name &&
            categoryRepository.existsByName(request.name)) {
            return Result.Failure(ProductError.CategoryNameAlreadyExists(request.name))
        }

        val updatedCategory = Category(
            categoryId = existingCategory.categoryId, // Preserve identity
            name = request.name,
            profitMargin = request.profitMargin,
            maxDiscount = request.maxDiscount,
            isActive = request.isActive
        )

        return try {
            val savedCategory = categoryRepository.update(updatedCategory)
            Result.Success(savedCategory)
        } catch (e: Exception) {
            Result.Failure(ProductError.DatabaseError("Failed to update category", e.message))
        }
    }

    private fun validateRequest(request: UpdateCategoryRequest): Result<Unit, ProductError> {

        if (request.name.isBlank()) {
            return Result.Failure(ProductError.InvalidCategoryName(request.name))
        }

        if (request.profitMargin < BigDecimal.ZERO) {
            return Result.Failure(ProductError.InvalidProfitMargin(request.profitMargin))
        }

        if (request.maxDiscount < BigDecimal.ZERO) {
            return Result.Failure(ProductError.InvalidMaxDiscount(request.maxDiscount))
        }

        if (request.maxDiscount > BigDecimal(100)) {
            return Result.Failure(ProductError.MaxDiscountExceeded(request.maxDiscount))
        }

        return Result.Success(Unit)
    }
}


data class UpdateCategoryRequest(
    val id: Long,
    val name: String,
    val profitMargin: BigDecimal,
    val maxDiscount: BigDecimal,
    val isActive: Boolean
)
