package com.foodcart.ecommerce.core.domain.user.model

/**
 * Enum representando os papéis/roles de usuário no sistema
 */
enum class UserRole {
    CUSTOMER,  // Cliente - pode visualizar produtos e fazer compras
    ADMIN      // Administrador - controle total do sistema
}
