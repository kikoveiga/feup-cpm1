package com.acme.supermarket.server.repository

import com.acme.supermarket.server.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUserUuid(userUuid: String): User?
}