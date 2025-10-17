package com.foodcart.ecommerce.adapters

import com.foodcart.ecommerce.core.domain.user.model.User
import com.foodcart.ecommerce.core.domain.user.model.UserRole
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
@ActiveProfiles("h2")
class UserRepositoryImplIntegrationTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var jpaRepository: UserJpaRepository

    private lateinit var userRepository: UserRepositoryImpl

    private fun setup() {
        userRepository = UserRepositoryImpl(jpaRepository)
    }

    @Test
    fun `should save and retrieve user by id`() {
        setup()

        val user = User.create("test@example.com", "Test User", "google123")
        val savedUser = userRepository.save(user)

        val foundUser = userRepository.findById(savedUser.id)

        assertNotNull(foundUser)
        assertEquals(savedUser.id, foundUser.id)
        assertEquals("test@example.com", foundUser.email)
        assertEquals("Test User", foundUser.name)
        assertEquals("google123", foundUser.googleId)
    }

    @Test
    fun `should save and retrieve user by email`() {
        setup()

        val user = User.create("test@example.com", "Test User", "google123")
        userRepository.save(user)

        val foundUser = userRepository.findByEmail("test@example.com")

        assertNotNull(foundUser)
        assertEquals("test@example.com", foundUser.email)
        assertEquals("Test User", foundUser.name)
    }

    @Test
    fun `should save and retrieve user by Google ID`() {
        setup()

        val user = User.create("test@example.com", "Test User", "google123")
        userRepository.save(user)

        val foundUser = userRepository.findByGoogleId("google123")

        assertNotNull(foundUser)
        assertEquals("google123", foundUser.googleId)
        assertEquals("Test User", foundUser.name)
    }

    @Test
    fun `should return null when user not found by id`() {
        setup()

        val foundUser = userRepository.findById("nonexistent")

        assertNull(foundUser)
    }

    @Test
    fun `should return null when user not found by email`() {
        setup()

        val foundUser = userRepository.findByEmail("nonexistent@example.com")

        assertNull(foundUser)
    }

    @Test
    fun `should return null when user not found by Google ID`() {
        setup()

        val foundUser = userRepository.findByGoogleId("nonexistent")

        assertNull(foundUser)
    }

    @Test
    fun `should check if email exists`() {
        setup()

        val user = User.create("test@example.com", "Test User", "google123")
        userRepository.save(user)

        assertTrue(userRepository.existsByEmail("test@example.com"))
        assertTrue(!userRepository.existsByEmail("nonexistent@example.com"))
    }

    @Test
    fun `should check if Google ID exists`() {
        setup()

        val user = User.create("test@example.com", "Test User", "google123")
        userRepository.save(user)

        assertTrue(userRepository.existsByGoogleId("google123"))
        assertTrue(!userRepository.existsByGoogleId("nonexistent"))
    }

    @Test
    fun `should find all users`() {
        setup()

        val user1 = User.create("user1@example.com", "User 1", "google1")
        val user2 = User.create("user2@example.com", "User 2", "google2")

        userRepository.save(user1)
        userRepository.save(user2)

        val allUsers = userRepository.findAll()

        assertEquals(2, allUsers.size)
        assertTrue(allUsers.any { it.email == "user1@example.com" })
        assertTrue(allUsers.any { it.email == "user2@example.com" })
    }

    @Test
    fun `should save admin user correctly`() {
        setup()

        val adminUser = User.create("admin@example.com", "Admin User", "adminGoogle", UserRole.ADMIN)
        val savedUser = userRepository.save(adminUser)

        assertEquals(UserRole.ADMIN, savedUser.role)

        val foundUser = userRepository.findById(savedUser.id)
        assertNotNull(foundUser)
        assertEquals(UserRole.ADMIN, foundUser.role)
    }
}
