package com.foodcart.ecommerce.core.domain.common.exception

import java.math.BigDecimal

class MaxDiscountExceededException(
    val maxDiscount: BigDecimal,
    message: String = "Max discount exceeded: $maxDiscount",
    cause: Throwable? = null
) : RuntimeException(message, cause)
