package com.foodcart.ecommerce.core.domain.user.service

import com.foodcart.ecommerce.core.domain.user.model.User
import com.foodcart.ecommerce.core.domain.user.model.UserRole
import com.foodcart.ecommerce.core.domain.user.port.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserDomainServiceInMemoryTest {

    private lateinit var userRepository: InMemoryUserRepository
    private lateinit var userDomainService: UserDomainService

    @BeforeEach
    fun setUp() {
        userRepository = InMemoryUserRepository()
        userDomainService = UserDomainService(userRepository)
    }

    @Test
    fun `should create new user from Google when no existing user found`() {
        val email = "test@example.com"
        val name = "Test User"
        val googleId = "google123"

        val result = userDomainService.createUserFromGoogle(email, name, googleId)

        assertNotNull(result)
        assertEquals(email, result.email)
        assertEquals(name, result.name)
        assertEquals(googleId, result.googleId)
        assertEquals(UserRole.CUSTOMER, result.role)
    }

    @Test
    fun `should throw exception when creating user with existing email`() {
        val email = "existing@example.com"
        val name = "Test User"
        val googleId = "google123"

        // Primeiro cria um usuário
        userDomainService.createUserFromGoogle(email, "Existing User", "existingGoogle")

        // Depois tenta criar outro com mesmo email
        try {
            userDomainService.createUserFromGoogle(email, name, googleId)
            assert(false) { "Should have thrown exception" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("Já existe um usuário com o email") == true)
        }
    }

    @Test
    fun `should throw exception when creating user with existing Google ID`() {
        val email = "test@example.com"
        val name = "Test User"
        val googleId = "existingGoogleId"

        // Primeiro cria um usuário
        userDomainService.createUserFromGoogle("other@example.com", "Other User", googleId)

        // Depois tenta criar outro com mesmo Google ID
        try {
            userDomainService.createUserFromGoogle(email, name, googleId)
            assert(false) { "Should have thrown exception" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("Já existe um usuário com o Google ID") == true)
        }
    }

    @Test
    fun `should find existing user by Google ID when using findOrCreateUserFromGoogle`() {
        val email = "test@example.com"
        val name = "Test User"
        val googleId = "existingGoogleId"

        // Primeiro cria um usuário
        val existingUser = userDomainService.createUserFromGoogle(email, name, googleId)

        // Depois tenta encontrar/criar o mesmo usuário
        val result = userDomainService.findOrCreateUserFromGoogle(email, name, googleId)

        assertEquals(existingUser.id, result.id)
        assertEquals(existingUser.email, result.email)
        assertEquals(existingUser.googleId, result.googleId)
    }

    @Test
    fun `should find existing user by email when Google ID not found but email exists`() {
        val email = "test@example.com"
        val name = "Test User"
        val googleId = "newGoogleId"

        // Primeiro cria um usuário
        val existingUser = userDomainService.createUserFromGoogle(email, name, "oldGoogleId")

        // Depois tenta encontrar/criar com novo Google ID mas mesmo email
        val result = userDomainService.findOrCreateUserFromGoogle(email, name, googleId)

        assertEquals(existingUser.id, result.id)
        assertEquals(existingUser.email, result.email)
    }

    @Test
    fun `should create new user when neither Google ID nor email exists`() {
        val email = "new@example.com"
        val name = "New User"
        val googleId = "newGoogleId"

        val result = userDomainService.findOrCreateUserFromGoogle(email, name, googleId)

        assertNotNull(result)
        assertEquals(email, result.email)
        assertEquals(name, result.name)
        assertEquals(googleId, result.googleId)
    }

    @Test
    fun `should update user role when requested by admin`() {
        // Cria um admin
        val adminUser = userDomainService.createUserFromGoogle(
            "admin@example.com",
            "Admin",
            "adminGoogle",
            UserRole.ADMIN
        )

        // Cria um usuário regular
        val regularUser = userDomainService.createUserFromGoogle(
            "user@example.com",
            "User",
            "userGoogle",
            UserRole.CUSTOMER
        )

        // Admin atualiza role do usuário
        val result = userDomainService.updateUserRole(regularUser.id, UserRole.ADMIN, adminUser.role)

        assertEquals(UserRole.ADMIN, result.role)
        assertEquals(regularUser.id, result.id)
    }

    @Test
    fun `should throw exception when non-admin tries to update user role`() {
        // Cria dois usuários comuns
        val customerUser = userDomainService.createUserFromGoogle(
            "customer@example.com",
            "Customer",
            "customerGoogle",
            UserRole.CUSTOMER
        )
        val otherUser = userDomainService.createUserFromGoogle(
            "other@example.com",
            "Other User",
            "otherGoogle",
            UserRole.CUSTOMER
        )

        // Usuário comum tenta alterar role
        try {
            userDomainService.updateUserRole(otherUser.id, UserRole.ADMIN, customerUser.role)
            assert(false) { "Should have thrown exception" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("Apenas administradores podem alterar papéis") == true)
        }
    }

    @Test
    fun `should throw exception when trying to update role of non-existent user`() {
        // Cria um admin
        val adminUser = userDomainService.createUserFromGoogle(
            "admin@example.com",
            "Admin",
            "adminGoogle",
            UserRole.ADMIN
        )

        // Tenta atualizar usuário inexistente
        try {
            userDomainService.updateUserRole("nonExistentUser", UserRole.ADMIN, adminUser.role)
            assert(false) { "Should have thrown exception" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("Usuário não encontrado") == true)
        }
    }
}

// In-memory implementation for testing
class InMemoryUserRepository : UserRepository {
    private val usersById = mutableMapOf<String, User>()
    private val usersByEmail = mutableMapOf<String, User>()
    private val usersByGoogleId = mutableMapOf<String, User>()

    override fun save(user: User): User {
        usersById[user.id] = user
        usersByEmail[user.email] = user
        usersByGoogleId[user.googleId] = user
        return user
    }

    override fun findById(id: String): User? = usersById[id]

    override fun findByEmail(email: String): User? = usersByEmail[email]

    override fun findByGoogleId(googleId: String): User? = usersByGoogleId[googleId]

    override fun findAll(): List<User> = usersById.values.toList()

    override fun existsByEmail(email: String): Boolean = usersByEmail.containsKey(email)

    override fun existsByGoogleId(googleId: String): Boolean = usersByGoogleId.containsKey(googleId)
}
