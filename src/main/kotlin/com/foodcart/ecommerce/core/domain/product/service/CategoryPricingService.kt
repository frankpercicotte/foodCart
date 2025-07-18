package com.foodcart.ecommerce.core.domain.product.service

import com.foodcart.ecommerce.core.domain.common.DomainError
import com.foodcart.ecommerce.core.domain.common.Result
import com.foodcart.ecommerce.core.domain.product.model.Category
import java.math.BigDecimal
import java.math.RoundingMode


class CategoryPricingService {

    fun calculateMarginAmount(category: Category, cost: BigDecimal): Result<BigDecimal> {
        if (cost < BigDecimal.ZERO) {
            return Result.Failure(DomainError.InvalidCost(cost))
        }
        try {
            val marginAmount = cost
                .multiply(category.profitMargin)
                .divide(BigDecimal(100), 2, RoundingMode.HALF_UP)

            return Result.Success(marginAmount)
        } catch (e: ArithmeticException) {
            return Result.Failure(
                DomainError.CalculationError("margin calculation", e.message ?: "Unknown arithmetic error")
            )
        }
    }

    fun calculateFinalPrice(category: Category, cost: BigDecimal): Result<BigDecimal> {
        if (cost < BigDecimal.ZERO) {
            return Result.Failure(DomainError.InvalidCost(cost))
        }
        if (!category.isActive) {
            return Result.Failure(DomainError.InactiveCategory(category.name))
        }

        return calculateMarginAmount(category, cost).map { marginAmount ->
            cost.add(marginAmount).setScale(2, RoundingMode.HALF_UP)
        }
    }

    fun calculateFinalPrices(category: Category, costs: List<BigDecimal>): Result<List<BigDecimal>> {
        val finalPrices = mutableListOf<BigDecimal>()

        for ((index, cost) in costs.withIndex()) {
            when (val result = calculateFinalPrice(category, cost)) {
                is Result.Success -> finalPrices.add(result.value)
                is Result.Failure -> return Result.Failure(
                    DomainError.CalculationError(
                        "batch price calculation",
                        "Failed at index $index: ${result.error.message}"
                    )
                )
            }
        }

        return Result.Success(finalPrices)
    }

    fun calculateActualMarginPercentage(cost: BigDecimal, finalPrice: BigDecimal): Result<BigDecimal> {
        if (cost <= BigDecimal.ZERO) {
            return Result.Failure(DomainError.ZeroCost(cost))
        }
        if (finalPrice < BigDecimal.ZERO) {
            return Result.Failure(DomainError.InvalidPrice(finalPrice))
        }

        try {
            val margin = finalPrice.subtract(cost)
            val percentage = margin
                .divide(cost, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP)

            return Result.Success(percentage)
        } catch (e: ArithmeticException) {
            return Result.Failure(
                DomainError.CalculationError("margin percentage calculation", e.message ?: "Division error")
            )
        }
    }

    fun validateCost(cost: BigDecimal): Result<Unit> {
        return if (cost >= BigDecimal.ZERO) {
            Result.Success(Unit)
        } else {
            Result.Failure(DomainError.InvalidCost(cost))
        }
    }
}