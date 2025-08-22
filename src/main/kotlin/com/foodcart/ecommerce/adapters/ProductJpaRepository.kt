
package com.foodcart.ecommerce.adapters

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductJpaRepository : JpaRepository<ProductEntity, Long> {
    fun existsByNormalizedNameAndCategoryId(normalizedName: String, categoryId: Long): Boolean
}