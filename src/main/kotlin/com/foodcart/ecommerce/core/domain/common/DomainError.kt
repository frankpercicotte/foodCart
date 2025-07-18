package com.foodcart.ecommerce.core.domain.common

import java.math.BigDecimal

sealed class DomainError(
    val code: String,
    val message: String,
    val context: Map<String, Any> = emptyMap()
) {

    data class InvalidCost(val cost: BigDecimal) : DomainError(
        code = "INVALID_COST",
        message = "Cost cannot be negative: $cost",
        context = mapOf("cost" to cost)
    )

    data class ZeroCost(val cost: BigDecimal) : DomainError(
        code = "ZERO_COST",
        message = "Cost must be positive for margin calculation: $cost",
        context = mapOf("cost" to cost)
    )

    data class InvalidDiscountPercentage(
        val discountPercentage: BigDecimal,
        val maxAllowed: BigDecimal,
        val categoryName: String
    ) : DomainError(
        code = "INVALID_DISCOUNT_PERCENTAGE",
        message = "Discount $discountPercentage% exceeds maximum $maxAllowed% for category '$categoryName'",
        context = mapOf(
            "discountPercentage" to discountPercentage,
            "maxAllowed" to maxAllowed,
            "categoryName" to categoryName
        )
    )

    data class NegativeDiscountPercentage(val discountPercentage: BigDecimal) : DomainError(
        code = "NEGATIVE_DISCOUNT_PERCENTAGE",
        message = "Discount cannot be negative: $discountPercentage%",
        context = mapOf("discountPercentage" to discountPercentage)
    )

    data class InvalidPrice(val price: BigDecimal) : DomainError(
        code = "INVALID_PRICE",
        message = "Price cannot be negative: $price",
        context = mapOf("price" to price)
    )

    data class InactiveCategory(val categoryName: String) : DomainError(
        code = "INACTIVE_CATEGORY",
        message = "Category '$categoryName' is not active",
        context = mapOf("categoryName" to categoryName)
    )

    data class CalculationError(val operation: String, val details: String) : DomainError(
        code = "CALCULATION_ERROR",
        message = "Error in $operation: $details",
        context = mapOf("operation" to operation, "details" to details)
    )
}