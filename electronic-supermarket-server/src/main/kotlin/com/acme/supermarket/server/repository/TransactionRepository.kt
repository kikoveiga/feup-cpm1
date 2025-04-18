package com.acme.supermarket.server.repository

import com.acme.supermarket.server.domain.Transaction
import com.acme.supermarket.server.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, String> {
    fun findByUserUserUuid(userUuid: String): List<Transaction>
}
