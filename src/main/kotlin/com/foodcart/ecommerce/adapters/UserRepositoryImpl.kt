package com.foodcart.ecommerce.adapters

import com.foodcart.ecommerce.core.domain.user.model.User
import com.foodcart.ecommerce.core.domain.user.port.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository
) : UserRepository {

    private val logger = LoggerFactory.getLogger(UserRepositoryImpl::class.java)

    override fun save(user: User): User {
        return runCatching {
            logger.debug("Saving user with email: ${user.email}")
            val entity = UserEntity.fromDomain(user)
            val saved = jpaRepository.save(entity)
            saved.toDomain()
        }.getOrElse { ex ->
            logger.error("Failed to save user with email: ${user.email}", ex)
            throw RuntimeException("Error saving user: ${ex.message}", ex)
        }
    }

    override fun findById(id: String): User? {
        return runCatching {
            logger.debug("Finding user by ID: $id")
            val entityId = id.toLongOrNull() ?: return null
            val entity = jpaRepository.findById(entityId).orElse(null) ?: return null
            entity.toDomain()
        }.getOrElse { ex ->
            logger.error("Failed to find user by ID: $id", ex)
            throw RuntimeException("Error finding user by ID: ${ex.message}", ex)
        }
    }

    override fun findByEmail(email: String): User? {
        return runCatching {
            logger.debug("Finding user by email: $email")
            jpaRepository.findByEmail(email)?.toDomain()
        }.getOrElse { ex ->
            logger.error("Failed to find user by email: $email", ex)
            throw RuntimeException("Error finding user by email: ${ex.message}", ex)
        }
    }

    override fun findByGoogleId(googleId: String): User? {
        return runCatching {
            logger.debug("Finding user by Google ID: $googleId")
            jpaRepository.findByGoogleId(googleId)?.toDomain()
        }.getOrElse { ex ->
            logger.error("Failed to find user by Google ID: $googleId", ex)
            throw RuntimeException("Error finding user by Google ID: ${ex.message}", ex)
        }
    }

    override fun findAll(): List<User> {
        return runCatching {
            logger.debug("Finding all users")
            jpaRepository.findAll().map { it.toDomain() }
        }.getOrElse { ex ->
            logger.error("Failed to find all users", ex)
            throw RuntimeException("Error finding all users: ${ex.message}", ex)
        }
    }

    override fun existsByEmail(email: String): Boolean {
        return runCatching {
            logger.debug("Checking if user exists by email: $email")
            jpaRepository.existsByEmail(email)
        }.getOrElse { ex ->
            logger.error("Failed to check if user exists by email: $email", ex)
            throw RuntimeException("Error checking user existence by email: ${ex.message}", ex)
        }
    }

    override fun existsByGoogleId(googleId: String): Boolean {
        return runCatching {
            logger.debug("Checking if user exists by Google ID: $googleId")
            jpaRepository.existsByGoogleId(googleId)
        }.getOrElse { ex ->
            logger.error("Failed to check if user exists by Google ID: $googleId", ex)
            throw RuntimeException("Error checking user existence by Google ID: ${ex.message}", ex)
        }
    }
}
