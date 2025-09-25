package com.foodcart.ecommerce.core.domain.common.exception

import java.math.BigDecimal

class InvalidPriceException(
    val price: BigDecimal,
    message: String = "Invalid product price: $price",
    cause: Throwable? = null
) : RuntimeException(message, cause)
