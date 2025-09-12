package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.exception.CategoryNotFoundException
import com.foodcart.ecommerce.core.domain.common.exception.DatabaseOperationException
import com.foodcart.ecommerce.core.domain.product.model.Category

interface GetCategoryUseCase {
    @Throws(DatabaseOperationException::class)
    fun findAll(): List<Category>
    
    @Throws(CategoryNotFoundException::class, DatabaseOperationException::class)
    fun findById(id: Long): Category
    
    @Throws(DatabaseOperationException::class)
    fun findAllActive(): List<Category>
}
