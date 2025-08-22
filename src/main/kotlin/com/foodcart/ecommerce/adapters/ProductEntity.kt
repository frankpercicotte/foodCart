package com.foodcart.ecommerce.adapters

import com.foodcart.ecommerce.core.domain.product.model.Product
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(
    name = "products",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_products_norm_category",
            columnNames = ["normalized_name", "category_id"]
        )
    ]
)
data class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(nullable = false)
    val name: String,
    
    @Column(name = "normalized_name", nullable = false)
    val normalizedName: String,
    
    @Column(nullable = false, length = 1000)
    val description: String,
    
    @Column(nullable = false, precision = 10, scale = 2)
    val price: BigDecimal,
    
    @Column(nullable = false, precision = 10, scale = 2)
    val cost: BigDecimal,
    
    @Column(nullable = false, precision = 10, scale = 2)
    val discount: BigDecimal = BigDecimal.ZERO,
    
    @Column(name = "category_id", nullable = false)
    val categoryId: Long,
    
    @Column(name = "stock_quantity", nullable = false)
    val stockQuantity: Int,
    
    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true,
    
    @Column(name = "image_url")
    val imageUrl: String? = null
) {
    fun toDomain(): Product = Product(
        productId = id,
        name = name,
        normalizedName = normalizedName,
        description = description,
        price = price,
        cost = cost,
        discount = discount,
        categoryId = categoryId,
        stockQuantity = stockQuantity,
        isActive = isActive,
        imageUrl = imageUrl
    )
    
    companion object {
        fun fromDomain(product: Product): ProductEntity = ProductEntity(
            id = product.productId,
            name = product.name,
            normalizedName = product.normalizedName,
            description = product.description,
            price = product.price,
            cost = product.cost,
            discount = product.discount,
            categoryId = product.categoryId,
            stockQuantity = product.stockQuantity,
            isActive = product.isActive,
            imageUrl = product.imageUrl
        )
    }
}