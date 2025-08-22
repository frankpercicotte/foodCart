package com.foodcart.ecommerce.adapters

import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val jpaRepository: ProductJpaRepository
) : ProductRepository {
    
    override fun save(product: Product): Product {
        val entity = ProductEntity.fromDomain(product)
        val saved = jpaRepository.save(entity)
        return saved.toDomain()
    }
    
    override fun existsByNormalizedNameAndCategoryId(normalizedName: String, categoryId: Long): Boolean {
        return jpaRepository.existsByNormalizedNameAndCategoryId(normalizedName, categoryId)
    }

    override fun findAll(): List<Product> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    override fun findById(productId: Long): Product? {
        return jpaRepository.findById(productId).map { it.toDomain() }.orElse(null)
    }
}