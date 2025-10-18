package com.foodcart.ecommerce.core.domain.user.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class UserRoleTest {

    @Test
    fun `should have correct enum values`() {
        val customer = UserRole.CUSTOMER
        val admin = UserRole.ADMIN

        assertEquals("CUSTOMER", customer.name)
        assertEquals("ADMIN", admin.name)
    }

    @Test
    fun `should have distinct enum values`() {
        assertNotEquals(UserRole.CUSTOMER, UserRole.ADMIN)
    }

    @Test
    fun `should be able to iterate over all values`() {
        val roles = UserRole.values()

        assertEquals(2, roles.size)
        assertEquals(UserRole.CUSTOMER, roles[0])
        assertEquals(UserRole.ADMIN, roles[1])
    }
}
