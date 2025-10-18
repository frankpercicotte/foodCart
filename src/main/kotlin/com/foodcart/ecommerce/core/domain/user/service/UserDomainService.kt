package com.foodcart.ecommerce.core.domain.user.service

import com.foodcart.ecommerce.core.domain.user.model.User
import com.foodcart.ecommerce.core.domain.user.model.UserRole
import com.foodcart.ecommerce.core.domain.user.port.UserRepository
import org.springframework.stereotype.Service

/**
 * Serviço de domínio para regras de negócio relacionadas a usuários
 */
@Service
class UserDomainService(private val userRepository: UserRepository) {

    /**
     * Cria um novo usuário a partir dos dados do Google OAuth2
     */
    fun createUserFromGoogle(
        email: String,
        name: String,
        googleId: String,
        role: UserRole = UserRole.CUSTOMER
    ): User {
        // Verifica se já existe usuário com mesmo email ou googleId
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("Já existe um usuário com o email: $email")
        }

        if (userRepository.existsByGoogleId(googleId)) {
            throw IllegalArgumentException("Já existe um usuário com o Google ID: $googleId")
        }

        val user = User.create(
            email = email,
            name = name,
            googleId = googleId,
            role = role
        )

        return userRepository.save(user)
    }

    /**
     * Busca ou cria usuário baseado nos dados do Google
     */
    fun findOrCreateUserFromGoogle(
        email: String,
        name: String,
        googleId: String
    ): User {
        return userRepository.findByGoogleId(googleId)
            ?: userRepository.findByEmail(email)
            ?: createUserFromGoogle(email, name, googleId)
    }

    /**
     * Atualiza o papel de um usuário (apenas admins podem fazer isso)
     */
    fun updateUserRole(userId: Long, newRole: UserRole, requestingUserRole: UserRole): User {
        if (requestingUserRole != UserRole.ADMIN) {
            throw IllegalArgumentException("Apenas administradores podem alterar papéis de usuários")
        }

        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("Usuário não encontrado com ID: $userId")

        val updatedUser = user.updateRole(newRole)
        return userRepository.save(updatedUser)
    }
}
