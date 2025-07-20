package com.foodcart.ecommerce.core.error

enum class ErrorCode(val code: String, val message: String) {
    INVALID_DISCOUNT_MAX_PERCENTAGE(
        "INVALID_DISCOUNT_PERCENTAGE",
        "Discount '%.2f%%' exceeds maximum '%.2f%%' for category '%s'"
    ),
    INVALID_COST(
        "INVALID_COST",
        "Cost cannot be negative: '%.2f%%'"
    ),
    NEGATIVE_DISCOUNT_PERCENTAGE(
        "NEGATIVE_DISCOUNT_PERCENTAGE",
    "Discount cannot be negative: '%.2f%%'"
    ),
    INVALID_PRICE(
      "INVALID_PRICE",
        "Price cannot be negative: '%.2f%%'"
    ),
    INACTIVE_CATEGORY(
        "INACTIVE_CATEGORY",
        "Category '%s' is not active"
    ),
    CALCULATION_ERROR(
        "CALCULATION_ERROR",
        "Error in '%s': '%s'"
    ),
    PROFIT_MARGIN_NEGATIVE(
        "PROFIT_MARGIN_NEGATIVE",
        "Profit margin cannot be negative: '%.2f%%'"
    ),
    MAXIMUS_DISCOUNT_NOT_NEGATIVE(
        "MAXIMUS_DISCOUNT_NOT_NEGATIVE",
    "Max discount cannot be negative: '%.2f%%'"
    );
    fun format(vararg args: Any?): String {
        return String.format(message, *args)
    }
}