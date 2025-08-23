package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.shared.Result

interface GetCategoryUseCase {
    fun findAll(): Result<List<Category>, ProductError>
    fun findById(id: Long): Result<Category, ProductError>
    fun findAllActive(): Result<List<Category>, ProductError>
}
