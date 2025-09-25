package com.foodcart.ecommerce.core.domain.common.exception

class CategoryNotFoundException(
    val categoryId: Long,
    message: String = "Category with id $categoryId not found"
) : RuntimeException(message)
