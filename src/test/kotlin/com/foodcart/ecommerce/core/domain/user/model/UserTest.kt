package com.foodcart.ecommerce.core.domain.user.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import java.time.LocalDateTime

class UserTest {

    @Test
    fun `should create user with valid data`() {
        val email = "test@example.com"
        val name = "Test User"
        val googleId = "google123"

        val user = User.create(email, name, googleId)

        assertEquals(email, user.email)
        assertEquals(name, user.name)
        assertEquals(googleId, user.googleId)
        assertEquals(UserRole.CUSTOMER, user.role)
        assertTrue(user.id.startsWith("user_"))
        assertTrue(user.createdAt.isBefore(LocalDateTime.now().plusSeconds(1)))
        assertTrue(user.updatedAt.isBefore(LocalDateTime.now().plusSeconds(1)))
    }

    @Test
    fun `should create admin user when role is specified`() {
        val email = "admin@example.com"
        val name = "Admin User"
        val googleId = "google456"

        val user = User.create(email, name, googleId, UserRole.ADMIN)

        assertEquals(UserRole.ADMIN, user.role)
    }

    @Test
    fun `should throw exception for blank email`() {
        assertThrows<IllegalArgumentException> {
            User.create("", "Test User", "google123")
        }
    }

    @Test
    fun `should throw exception for blank name`() {
        assertThrows<IllegalArgumentException> {
            User.create("test@example.com", "", "google123")
        }
    }

    @Test
    fun `should throw exception for blank googleId`() {
        assertThrows<IllegalArgumentException> {
            User.create("test@example.com", "Test User", "")
        }
    }

    @Test
    fun `should update role correctly`() {
        val user = User.create("test@example.com", "Test User", "google123")

        val updatedUser = user.updateRole(UserRole.ADMIN)

        assertEquals(UserRole.ADMIN, updatedUser.role)
        assertNotEquals(user.updatedAt, updatedUser.updatedAt)
        assertEquals(user.id, updatedUser.id)
        assertEquals(user.email, updatedUser.email)
        assertEquals(user.name, updatedUser.name)
        assertEquals(user.googleId, updatedUser.googleId)
    }
}
