package com.foodcart.ecommerce.core.domain.product.service

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.shared.Result
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.error.ErrorMessages
import com.foodcart.ecommerce.core.shared.validation.validate
import java.math.BigDecimal
import java.math.RoundingMode


class CategoryPricingService {

    fun calculateMarginAmount(category: Category, cost: BigDecimal): Result<BigDecimal, ProductError> {
        if (cost < BigDecimal.ZERO) {
            return Result.Failure(ProductError.InvalidCost(cost))
        }
        try {
            val marginAmount = cost
                .multiply(category.profitMargin)
                .divide(BigDecimal(100), 2, RoundingMode.HALF_UP)

            return Result.Success(marginAmount)
        } catch (e: ArithmeticException) {
            return Result.Failure(
                ProductError.CalculationError(
                    ErrorMessages.MARGIN_CALCULATION,
                    e.message ?: ErrorMessages.UNKNOWN_ARITHMETIC_ERROR
                )
            )
        }
    }

    fun calculateFinalPriceOne(category: Category, cost: BigDecimal): Result<BigDecimal, ProductError> {
        if (cost < BigDecimal.ZERO) {
            return Result.Failure(ProductError.InvalidCost(cost))
        }
        if (!category.isActive) {
            return Result.Failure(ProductError.InactiveCategory(category.name))
        }

        return calculateMarginAmount(category, cost).map { marginAmount ->
            cost.add(marginAmount).setScale(2, RoundingMode.HALF_UP)
        }
    }

    fun calculateFinalPrice(category: Category, cost: BigDecimal): Result<BigDecimal, ProductError> {
        return validateCost(cost)
            .flatMap { isActiveCategory(category) }
            .flatMap { calculateMarginAmount(category, cost) }
            .map { marginAmount ->
                cost.add(marginAmount).setScale(2, RoundingMode.HALF_UP)
            }
    }


    fun calculateFinalPrices(category: Category, costs: List<BigDecimal>): Result<List<BigDecimal>, ProductError> {
        val finalPrices = mutableListOf<BigDecimal>()

        for ((index, cost) in costs.withIndex()) {
            when (val result = calculateFinalPrice(category, cost)) {
                is Result.Success -> finalPrices.add(result.value)
                is Result.Failure -> return Result.Failure(
                    ProductError.CalculationError(
                        "batch price calculation",
                        "Failed at index $index: ${result.error.message}"
                    )
                )
            }
        }

        return Result.Success(finalPrices)
    }

    fun calculateActualMarginPercentage(cost: BigDecimal, finalPrice: BigDecimal): Result<BigDecimal, ProductError> {
        return validateCost(cost)
            .flatMap { validatePrice(finalPrice) }
            .flatMap {
                runCatching {
                    val margin = finalPrice.subtract(cost)
                    margin.divide(cost, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal(100))
                        .setScale(2, RoundingMode.HALF_UP)
                }.fold(
                    onSuccess = { Result.Success(it) },
                    onFailure = { throwable ->
                        Result.Failure(ProductError.CalculationError(
                            "Margin calculation failed", throwable.message ?: "Unknown error"))
                    }
                )
            }
    }

    fun validateCost(cost: BigDecimal): Result<Unit, ProductError> =
        validate(cost > BigDecimal.ZERO) { ProductError.InvalidCost(cost) }

    fun validatePrice(price: BigDecimal): Result<Unit, ProductError> =
        validate(price >= BigDecimal.ZERO) { ProductError.InvalidPrice(price) }

    fun isActiveCategory(category: Category): Result<Unit, ProductError> =
        validate(category.isActive) { ProductError.InactiveCategory(category.name) }
}
