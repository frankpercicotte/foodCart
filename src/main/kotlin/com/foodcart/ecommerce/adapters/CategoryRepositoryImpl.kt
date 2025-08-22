package com.foodcart.ecommerce.adapters

import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryImpl(
    private val jpaRepository: CategoryJpaRepository
) : CategoryRepository {
    
    override fun findById(id: Long): Category? {
        return jpaRepository.findById(id).orElse(null)?.toDomain()
    }
    
    override fun findByName(name: String): Category? {
        return jpaRepository.findByName(name)?.toDomain()
    }
    
    override fun findAllActive(): List<Category> {
        return jpaRepository.findAllActive().map { it.toDomain() }
    }
    
    override fun findAll(): List<Category> {
        return jpaRepository.findAll().map { it.toDomain() }
    }
    
    override fun save(category: Category): Category {
        val entity = CategoryEntity.fromDomain(category)
        val saved = jpaRepository.save(entity)
        return saved.toDomain()
    }
    
    override fun update(category: Category): Category {
        val entity = CategoryEntity.fromDomain(category)
        val updated = jpaRepository.save(entity)
        return updated.toDomain()
    }
    
    override fun deactivate(id: Long): Boolean {
        val entity = jpaRepository.findById(id).orElse(null) ?: return false
        val deactivated = entity.copy(isActive = false)
        jpaRepository.save(deactivated)
        return true
    }
    
    override fun existsByName(name: String): Boolean {
        return jpaRepository.existsByName(name)
    }
}