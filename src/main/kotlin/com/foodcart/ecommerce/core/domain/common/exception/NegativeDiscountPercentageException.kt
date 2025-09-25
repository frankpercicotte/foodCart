package com.foodcart.ecommerce.core.domain.common.exception

import java.math.BigDecimal

class NegativeDiscountPercentageException(
    val discountPercentage: BigDecimal,
    message: String = "Negative discount percentage: $discountPercentage",
    cause: Throwable? = null
) : RuntimeException(message, cause)
