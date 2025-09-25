package com.foodcart.ecommerce.core.domain.common.exception

import java.math.BigDecimal

class InvalidDiscountPercentageException(
    val discountPercentage: BigDecimal,
    val maxAllowed: BigDecimal,
    val categoryName: String,
    message: String = "Invalid discount percentage: $discountPercentage (max allowed: $maxAllowed) for category '$categoryName'",
    cause: Throwable? = null
) : RuntimeException(message, cause)
