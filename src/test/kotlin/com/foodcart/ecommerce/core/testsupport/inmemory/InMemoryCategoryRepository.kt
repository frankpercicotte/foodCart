package com.foodcart.ecommerce.core.testsupport.inmemory

import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository

class InMemoryCategoryRepository : CategoryRepository {
    private val categories = mutableMapOf<Long, Category>()
    private var nextId = 1L

    override fun findById(id: Long): Category? = categories[id]
    override fun findByName(name: String): Category? = categories.values.firstOrNull { it.name == name }
    override fun findAllActive(): List<Category> = categories.values.filter { it.isActive }
    override fun findAll(): List<Category> = categories.values.toList()
    override fun save(category: Category): Category {
        val id = category.categoryId ?: nextId++
        val saved = Category(id, category.name, category.profitMargin, category.maxDiscount, category.isActive)
        categories[id] = saved
        return saved
    }
    override fun update(category: Category): Category {
        val id = category.categoryId ?: return category
        categories[id] = category
        return category
    }
    override fun deactivate(id: Long): Boolean {
        val cat = categories[id] ?: return false
        categories[id] = Category(cat.categoryId, cat.name, cat.profitMargin, cat.maxDiscount, false)
        return true
    }
    override fun existsByName(name: String): Boolean = categories.values.any { it.name == name }

    fun reset() {
        categories.clear()
        nextId = 1L
    }
}