package com.foodcart.ecommerce.core.domain.common.exception

class DatabaseOperationException(
    val operation: String,
    message: String = "Failed to execute database operation: $operation",
    cause: Throwable? = null
) : RuntimeException(message, cause)
