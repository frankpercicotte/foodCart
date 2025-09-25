package com.foodcart.ecommerce.core.domain.common.exception

class InvalidProductNameException(
    val name: String,
    message: String = "Invalid product name: '$name'",
    cause: Throwable? = null
) : RuntimeException(message, cause)
