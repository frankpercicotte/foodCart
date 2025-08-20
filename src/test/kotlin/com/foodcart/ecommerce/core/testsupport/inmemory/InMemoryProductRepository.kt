package com.foodcart.ecommerce.core.testsupport.inmemory

import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository

class InMemoryProductRepository : ProductRepository {

    private val products = mutableListOf<Product>()
    private var nextId = 1L

    @Synchronized
    override fun save(product: Product): Product {
        val id = product.productId ?: nextId.toString().also { nextId++ }
        val saved = product.copy(productId = id)
        products.removeIf { it.productId == id }
        products.add(saved)
        return saved
    }

    @Synchronized
    override fun existsByNormalizedNameAndCategoryId(normalizedName: String, categoryId: String): Boolean {
        return products.any { it.normalizedName == normalizedName && it.categoryId == categoryId }
    }

    fun findAll(): List<Product> = products.toList()

    @Synchronized
    fun clear() { products.clear() }

    @Synchronized
    fun reset() { products.clear(); nextId = 1L }
}