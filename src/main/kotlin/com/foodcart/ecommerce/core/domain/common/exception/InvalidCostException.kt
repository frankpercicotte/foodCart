package com.foodcart.ecommerce.core.domain.common.exception

import java.math.BigDecimal

class InvalidCostException(
    val cost: BigDecimal,
    message: String = "Invalid cost: $cost",
    cause: Throwable? = null
) : RuntimeException(message, cause)
