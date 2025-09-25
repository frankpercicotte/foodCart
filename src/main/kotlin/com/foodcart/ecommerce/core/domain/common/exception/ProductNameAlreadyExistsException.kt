package com.foodcart.ecommerce.core.domain.common.exception

class ProductNameAlreadyExistsException(
    val name: String,
    message: String = "Product name already exists: '$name'",
    cause: Throwable? = null
) : RuntimeException(message, cause)
