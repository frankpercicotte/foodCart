package com.foodcart.ecommerce.core.domain.common.exception

class InvalidCategoryNameException(
    val name: String,
    message: String = "Invalid category name: '$name'",
    cause: Throwable? = null
) : RuntimeException(message, cause)
