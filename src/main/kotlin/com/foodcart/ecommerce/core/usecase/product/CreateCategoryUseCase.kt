package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.shared.Result
import java.math.BigDecimal


class CreateCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {

    fun execute(request: CreateCategoryRequest): Result<Category, ProductError> {
        val validationResult = validateRequest(request)

        if (validationResult.isFailure()) return validationResult as Result.Failure<Category, ProductError>

        if (categoryRepository.existsByName(request.name)) {
            return Result.Failure(ProductError.CategoryNameAlreadyExists(request.name))
        }

        val category = Category(
            categoryId = null,
            name = request.name,
            profitMargin = request.profitMargin,
            maxDiscount = request.maxDiscount,
            isActive = request.isActive
        )

        return try {
            val savedCategory = categoryRepository.save(category)
            Result.Success(savedCategory)
        } catch (e: Exception) {
            Result.Failure(ProductError.DatabaseError("Failed to save category", e.message))
        }
    }

    private fun validateRequest(request: CreateCategoryRequest): Result<Unit, ProductError> {

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

data class CreateCategoryRequest(
    val name: String,
    val profitMargin: BigDecimal,
    val maxDiscount: BigDecimal,
    val isActive: Boolean = true
)
