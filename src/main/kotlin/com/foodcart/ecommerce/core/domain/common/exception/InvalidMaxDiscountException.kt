package com.foodcart.ecommerce.core.domain.common.exception

import java.math.BigDecimal

class InvalidMaxDiscountException(
    val maxDiscount: BigDecimal,
    message: String = "Invalid max discount: $maxDiscount",
    cause: Throwable? = null
) : RuntimeException(message, cause)
