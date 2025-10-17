package com.foodcart.ecommerce.adapters

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun findByGoogleId(googleId: String): UserEntity?
    fun existsByEmail(email: String): Boolean
    fun existsByGoogleId(googleId: String): Boolean
}
