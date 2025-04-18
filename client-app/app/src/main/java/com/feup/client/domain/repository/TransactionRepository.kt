package com.feup.client.domain.repository

import com.feup.client.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getLocalTransactions(): Flow<List<Transaction>>
    suspend fun fetchAndStoreRemoteTransactions(userUuid: String)
}