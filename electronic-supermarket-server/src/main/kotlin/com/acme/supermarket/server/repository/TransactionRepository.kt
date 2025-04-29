package com.acme.supermarket.server.repository

import com.acme.supermarket.server.domain.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface TransactionRepository : JpaRepository<Transaction, String> {
    fun findByUserUserUuid(userUuid: String): List<Transaction>
    fun findByUserUserUuidAndTimestamp(userUuid: String?, timestamp: LocalDateTime?): List<Transaction?>?

}
