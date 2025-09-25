package com.foodcart.ecommerce.core.domain.common.exception

import java.math.BigDecimal

class InvalidProfitMarginException(
    val profitMargin: BigDecimal,
    message: String = "Invalid profit margin: $profitMargin",
    cause: Throwable? = null
) : RuntimeException(message, cause)
