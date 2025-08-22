

package com.foodcart.ecommerce.adapters

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CategoryJpaRepository : JpaRepository<CategoryEntity, Long> {
    fun findByName(name: String): CategoryEntity?
    
    @Query("SELECT c FROM CategoryEntity c WHERE c.isActive = true")
    fun findAllActive(): List<CategoryEntity>
    
    fun existsByName(name: String): Boolean
}