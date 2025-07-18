package com.foodcart.ecommerce.core.domain.product.port

import com.foodcart.ecommerce.core.domain.product.model.Category

interface CategoryRepository {
    fun findById(id: Long): Category?
    fun findByName(name: String): Category?
    fun findAllActive(): List<Category>
    fun findAll(): List<Category>
    fun save(category: Category): Category
    fun update(category: Category): Category
    fun deactivate(id: Long): Boolean
    fun existsByName(name: String): Boolean
}