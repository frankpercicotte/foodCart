package com.foodcart.ecommerce.adapters

import com.foodcart.ecommerce.core.domain.user.model.User
import com.foodcart.ecommerce.core.domain.user.model.UserRole
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRole,

    @Column(name = "google_id", nullable = false, unique = true)
    val googleId: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    fun toDomain(): User = User(
        id = id,
        email = email,
        name = name,
        role = role,
        googleId = googleId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(user: User): UserEntity = UserEntity(
            id = user.id,
            email = user.email,
            name = user.name,
            role = user.role,
            googleId = user.googleId,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}
