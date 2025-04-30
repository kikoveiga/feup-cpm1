package com.feup.client.domain.repository

import com.feup.client.domain.model.Transaction

interface TransactionRepository {
    fun getLocalTransactions(userUuid: String): List<Transaction>
    suspend fun fetchAndStoreRemoteTransactions(userUuid: String, nonce: String, signature: String): Result<Unit>
}