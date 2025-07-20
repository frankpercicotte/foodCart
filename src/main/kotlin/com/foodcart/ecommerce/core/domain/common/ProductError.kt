package com.foodcart.ecommerce.core.domain.common

import com.foodcart.ecommerce.core.error.AppError
import com.foodcart.ecommerce.core.error.ErrorCode
import java.math.BigDecimal

sealed class ProductError(
    code: String,
    message: String,
    context: Map<String, Any> = emptyMap()
): AppError(code, message, context){

    data class InvalidCost(val cost: BigDecimal) : ProductError(
        code = ErrorCode.INVALID_COST.code,
        message = ErrorCode.INVALID_COST.message.format(cost.toDouble()),
        context = mapOf("cost" to cost)
    )

    data class InvalidDiscountPercentage(
        val discountPercentage: BigDecimal,
        val maxAllowed: BigDecimal,
        val categoryName: String
    ) : ProductError(
        code = ErrorCode.INVALID_DISCOUNT_MAX_PERCENTAGE.code,
        message = ErrorCode.INVALID_DISCOUNT_MAX_PERCENTAGE.format(discountPercentage.toDouble(),maxAllowed.toDouble(),categoryName),
        context = mapOf(
            "discountPercentage" to discountPercentage,
            "maxAllowed" to maxAllowed,
            "categoryName" to categoryName
        )
    )

    data class NegativeDiscountPercentage(val discountPercentage: BigDecimal) : ProductError(
        code = ErrorCode.NEGATIVE_DISCOUNT_PERCENTAGE.code,
        message = ErrorCode.NEGATIVE_DISCOUNT_PERCENTAGE.message.format(discountPercentage.toDouble()),
        context = mapOf("discountPercentage" to discountPercentage)
    )

    data class InvalidPrice(val price: BigDecimal) : ProductError(
        code = ErrorCode.INVALID_PRICE.code,
        message = ErrorCode.INVALID_PRICE.message.format(price.toDouble()),
        context = mapOf("price" to price)
    )

    data class InactiveCategory(val categoryName: String) : ProductError(
        code = ErrorCode.INACTIVE_CATEGORY.code,
        message = ErrorCode.INACTIVE_CATEGORY.message.format(categoryName),
        context = mapOf("categoryName" to categoryName)
    )

    data class CalculationError(val operation: String, val details: String) : ProductError(
        code = ErrorCode.CALCULATION_ERROR.code,
        message = ErrorCode.CALCULATION_ERROR.message.format(operation,details),
        context = mapOf("operation" to operation, "details" to details)
    )
}