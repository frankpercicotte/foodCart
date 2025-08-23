package com.foodcart.ecommerce.adapters

import com.foodcart.ecommerce.core.domain.product.model.Category
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "categories")
data class CategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(nullable = false, unique = true)
    val name: String,
    
    @Column(name = "profit_margin", nullable = false, precision = 5, scale = 2)
    val profitMargin: BigDecimal,
    
    @Column(name = "max_discount", nullable = false, precision = 5, scale = 2)
    val maxDiscount: BigDecimal,
    
    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true
) {
    fun toDomain(): Category = Category(
        categoryId = id ?: throw IllegalStateException("Category ID cannot be null when converting to domain"),
        name = name,
        profitMargin = profitMargin,
        maxDiscount = maxDiscount,
        isActive = isActive
    )
    
    companion object {
        fun fromDomain(category: Category): CategoryEntity = CategoryEntity(
            id = category.categoryId,
            name = category.name,
            profitMargin = category.profitMargin,
            maxDiscount = category.maxDiscount,
            isActive = category.isActive
        )
    }
}