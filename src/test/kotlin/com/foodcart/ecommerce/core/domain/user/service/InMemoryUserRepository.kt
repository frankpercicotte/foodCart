package com.foodcart.ecommerce.core.domain.user.service

import com.foodcart.ecommerce.core.domain.user.model.User
import com.foodcart.ecommerce.core.domain.user.port.UserRepository


class InMemoryUserRepository : UserRepository {
    private val usersById = mutableMapOf<Long, User>()
    private val usersByEmail = mutableMapOf<String, User>()
    private val usersByGoogleId = mutableMapOf<String, User>()

    override fun save(user: User): User {
        usersById[user.id] = user
        usersByEmail[user.email] = user
        usersByGoogleId[user.googleId] = user
        return user
    }

    override fun findById(id: Long): User? = usersById[id]

    override fun findByEmail(email: String): User? = usersByEmail[email]

    override fun findByGoogleId(googleId: String): User? = usersByGoogleId[googleId]

    override fun findAll(): List<User> = usersById.values.toList()

    override fun existsByEmail(email: String): Boolean = usersByEmail.containsKey(email)

    override fun existsByGoogleId(googleId: String): Boolean = usersByGoogleId.containsKey(googleId)
}
