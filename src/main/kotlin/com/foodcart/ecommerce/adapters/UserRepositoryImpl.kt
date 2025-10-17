package com.foodcart.ecommerce.adapters

import com.foodcart.ecommerce.core.domain.user.model.User
import com.foodcart.ecommerce.core.domain.user.port.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository
) : UserRepository {

    override fun save(user: User): User {
        val entity = UserEntity.fromDomain(user)
        val saved = jpaRepository.save(entity)
        return saved.toDomain()
    }

    override fun findById(id: String): User? {
        val entityId = id.toLongOrNull() ?: return null
        return jpaRepository.findById(entityId).orElse(null)?.toDomain()
    }

    override fun findByEmail(email: String): User? {
        return jpaRepository.findByEmail(email)?.toDomain()
    }

    override fun findByGoogleId(googleId: String): User? {
        return jpaRepository.findByGoogleId(googleId)?.toDomain()
    }

    override fun findAll(): List<User> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    override fun existsByEmail(email: String): Boolean {
        return jpaRepository.existsByEmail(email)
    }

    override fun existsByGoogleId(googleId: String): Boolean {
        return jpaRepository.existsByGoogleId(googleId)
    }
}
