package com.foodcart.ecommerce.core.domain.common.exception

class CalculationException(
    val operation: String,
    val details: String,
    message: String = "Calculation error on $operation: $details",
    cause: Throwable? = null
) : RuntimeException(message, cause)
