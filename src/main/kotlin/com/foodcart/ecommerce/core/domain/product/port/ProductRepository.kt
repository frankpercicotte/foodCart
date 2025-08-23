package com.foodcart.ecommerce.core.domain.product.port

import com.foodcart.ecommerce.core.domain.product.model.Product

interface ProductRepository {
    fun save(product: Product): Product
    fun existsByNormalizedNameAndCategoryId(
        normalizedName: String,
        categoryId: Long,
    ): Boolean
    fun findAll(): List<Product>
    fun findById(productId: Long): Product?

}