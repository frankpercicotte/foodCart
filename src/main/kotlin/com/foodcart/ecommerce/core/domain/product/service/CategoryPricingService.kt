package com.foodcart.ecommerce.core.domain.product.service

import com.foodcart.ecommerce.core.domain.common.exception.CalculationException
import com.foodcart.ecommerce.core.domain.common.exception.InactiveCategoryException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidCostException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidPriceException
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.error.ErrorMessages
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class CategoryPricingService {

    fun calculateMarginAmount(category: Category, cost: BigDecimal): BigDecimal {
        if (cost <= BigDecimal.ZERO) {
            throw InvalidCostException(cost)
        }
        return try {
            cost
                .multiply(category.profitMargin)
                .divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
        } catch (e: ArithmeticException) {
            throw CalculationException(
                operation = ErrorMessages.MARGIN_CALCULATION,
                details = e.message ?: ErrorMessages.UNKNOWN_ARITHMETIC_ERROR,
                cause = e
            )
        }
    }

    fun calculateFinalPriceOne(category: Category, cost: BigDecimal): BigDecimal {
        if (cost <= BigDecimal.ZERO) {
            throw InvalidCostException(cost)
        }
        if (!category.isActive) {
            throw InactiveCategoryException(category.name)
        }
        val marginAmount = calculateMarginAmount(category, cost)
        return cost.add(marginAmount).setScale(2, RoundingMode.HALF_UP)
    }

    fun calculateFinalPrice(category: Category, cost: BigDecimal): BigDecimal =
        calculateFinalPriceOne(category, cost)

    fun calculateFinalPrices(category: Category, costs: List<BigDecimal>): List<BigDecimal> =
        costs.mapIndexed { _, cost -> calculateFinalPrice(category, cost) }

    fun calculateActualMarginPercentage(cost: BigDecimal, finalPrice: BigDecimal): BigDecimal {
        if (cost <= BigDecimal.ZERO) {
            throw InvalidCostException(cost)
        }
        if (finalPrice < BigDecimal.ZERO) {
            throw InvalidPriceException(finalPrice)
        }
        return try {
            val margin = finalPrice.subtract(cost)
            margin.divide(cost, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP)
        } catch (e: ArithmeticException) {
            throw CalculationException(
                operation = "Margin calculation failed",
                details = e.message ?: "Unknown error",
                cause = e
            )
        }
    }

}
