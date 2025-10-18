package com.foodcart.ecommerce.core.domain.user.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime


data class User(
    val id: Long,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must have a valid format")
    val email: String,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,

    val role: UserRole,

    @field:NotBlank(message = "Google ID is required")
    val googleId: String,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun create(
            email: String,
            name: String,
            googleId: String,
            role: UserRole = UserRole.CUSTOMER
        ): User {
            val now = LocalDateTime.now()
            return User(
                id = 0L,
                email = email,
                name = name,
                role = role,
                googleId = googleId,
                createdAt = now,
                updatedAt = now
            )
        }
    }

    fun updateRole(newRole: UserRole): User {
        return this.copy(role = newRole, updatedAt = LocalDateTime.now())
    }
}
