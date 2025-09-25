package com.foodcart.ecommerce.core.domain.common.exception

class InactiveCategoryException(
    val categoryName: String,
    message: String = "Inactive category: '$categoryName'",
    cause: Throwable? = null
) : RuntimeException(message, cause)
