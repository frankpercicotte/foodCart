package com.foodcart.ecommerce.core.domain.common.exception

class CategoryAlreadyExistsException(
    val categoryName: String,
    message: String = "Category with name '$categoryName' already exists"
) : RuntimeException(message)
