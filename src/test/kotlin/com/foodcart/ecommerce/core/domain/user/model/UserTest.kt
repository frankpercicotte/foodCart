package com.foodcart.ecommerce.core.domain.user.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import java.time.LocalDateTime

class UserTest {

    @Test
    fun `should create user with valid data - jakarta validation will handle blank checks`() {
        val email = "test@example.com"
        val name = "Test User"
        val googleId = "google123"

        val user = User.create(email, name, googleId)

        assertEquals(email, user.email)
        assertEquals(name, user.name)
        assertEquals(googleId, user.googleId)
        assertEquals(UserRole.CUSTOMER, user.role)
        assertTrue(user.id >= 0L) // ID pode ser 0L (não salvo) ou positivo (salvo)
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


    @Test
    fun `should validate jakarta email format annotation exists on User class`() {
        val userClass = User::class.java
        val emailField = userClass.getDeclaredField("email")

        val emailAnnotation = emailField.getAnnotation(jakarta.validation.constraints.Email::class.java)
        assertNotEquals(null, emailAnnotation, "Campo email deve ter annotation @Email")
    }

    @Test
    fun `should validate jakarta notblank annotations exist on User class`() {
        val userClass = User::class.java

        val emailField = userClass.getDeclaredField("email")
        val nameField = userClass.getDeclaredField("name")
        val googleIdField = userClass.getDeclaredField("googleId")

        assertNotEquals(null, emailField.getAnnotation(jakarta.validation.constraints.NotBlank::class.java))
        assertNotEquals(null, nameField.getAnnotation(jakarta.validation.constraints.NotBlank::class.java))
        assertNotEquals(null, googleIdField.getAnnotation(jakarta.validation.constraints.NotBlank::class.java))
    }

    @Test
    fun `should validate jakarta size annotation exists on name field`() {
        val userClass = User::class.java
        val nameField = userClass.getDeclaredField("name")

        val sizeAnnotation = nameField.getAnnotation(jakarta.validation.constraints.Size::class.java)
        assertNotEquals(null, sizeAnnotation, "Campo name deve ter annotation @Size")
        assertEquals(2, sizeAnnotation.min)
        assertEquals(100, sizeAnnotation.max)
    }


    @Test
    fun `should handle email with special characters correctly`() {
        val email = "test.email+tag@example-site.com"
        val name = "Test User"
        val googleId = "google123"

        val user = User.create(email, name, googleId)

        assertEquals(email, user.email)
        assertTrue(user.email.contains("+"))
        assertTrue(user.email.contains("-"))
    }

    @Test
    fun `should handle name with unicode characters correctly`() {
        val email = "test@example.com"
        val name = "José María François"
        val googleId = "google123"

        val user = User.create(email, name, googleId)

        assertEquals(name, user.name)
        assertTrue(user.name.contains("José"))
        assertTrue(user.name.contains("é"))
        assertTrue(user.name.contains("ç"))
    }

    @Test
    fun `should handle name at minimum length boundary`() {
        val email = "test@example.com"
        val name = "Jo"
        val googleId = "google123"

        val user = User.create(email, name, googleId)

        assertEquals(name, user.name)
        assertEquals(2, user.name.length)
    }

    @Test
    fun `should handle name at maximum length boundary`() {
        val email = "test@example.com"
        val name = "a".repeat(100)
        val googleId = "google123"

        val user = User.create(email, name, googleId)

        assertEquals(name, user.name)
        assertEquals(100, user.name.length)
    }


    @Test
    fun `should implement equals correctly for identical users`() {
        val email = "test@example.com"
        val name = "Test User"
        val googleId = "google123"
        val now = LocalDateTime.now()

        val user1 = User(
            id = 1L,
            email = email,
            name = name,
            role = UserRole.CUSTOMER,
            googleId = googleId,
            createdAt = now,
            updatedAt = now
        )

        val user2 = User(
            id = 1L,
            email = email,
            name = name,
            role = UserRole.CUSTOMER,
            googleId = googleId,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(user1, user2)
        assertEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun `should implement not equals correctly for different users`() {
        val user1 = User.create("test1@example.com", "Test User 1", "google1")
        val user2 = User.create("test2@example.com", "Test User 2", "google2")

        assertNotEquals(user1, user2)
        assertNotEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun `should generate meaningful toString representation`() {
        val user = User.create("test@example.com", "Test User", "google123")

        val toString = user.toString()

        assertTrue(toString.contains("User("))
        assertTrue(toString.contains("test@example.com"))
        assertTrue(toString.contains("Test User"))
        assertTrue(toString.contains("CUSTOMER"))
        assertTrue(toString.contains("google123"))
    }

}

