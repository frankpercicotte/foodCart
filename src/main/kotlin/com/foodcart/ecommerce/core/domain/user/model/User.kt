package com.foodcart.ecommerce.core.domain.user.model

import java.time.LocalDateTime

/**
 * Entidade de domínio representando um usuário do sistema
 */
data class User(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val googleId: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    init {
        require(email.isNotBlank()) { "Email não pode ser vazio" }
        require(name.isNotBlank()) { "Nome não pode ser vazio" }
        require(googleId.isNotBlank()) { "Google ID não pode ser vazio" }
    }

    companion object {
        fun create(
            email: String,
            name: String,
            googleId: String,
            role: UserRole = UserRole.CUSTOMER
        ): User {
            val now = LocalDateTime.now()
            return User(
                id = generateId(),
                email = email,
                name = name,
                role = role,
                googleId = googleId,
                createdAt = now,
                updatedAt = now
            )
        }

        private fun generateId(): String {
            return "user_${System.currentTimeMillis()}_${(Math.random() * 1000).toInt()}"
        }
    }

    fun updateRole(newRole: UserRole): User {
        return this.copy(role = newRole, updatedAt = LocalDateTime.now())
    }
}
