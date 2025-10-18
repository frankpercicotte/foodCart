package com.foodcart.ecommerce.core.domain.user.port

import com.foodcart.ecommerce.core.domain.user.model.User

interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
    fun findByGoogleId(googleId: String): User?
    fun findAll(): List<User>
    fun existsByEmail(email: String): Boolean
    fun existsByGoogleId(googleId: String): Boolean
}
