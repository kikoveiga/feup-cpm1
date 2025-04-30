package com.feup.client.domain.usecases

import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.Transaction
import com.feup.client.domain.repository.TransactionRepository
import java.util.UUID
import javax.inject.Inject

class FetchTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val cryptoManager: CryptoManager
) {
    fun local(userUuid: String): List<Transaction> {
        return transactionRepository.getLocalTransactions(userUuid)
    }

    suspend fun remote(userUuid: String): Result<List<Transaction>> {
        val nonce = UUID.randomUUID().toString().replace("-", "")
        val message = "userUuid:$userUuid&nonce:$nonce"
        val signature = cryptoManager.generateSignature(userUuid, message.toByteArray())

        return transactionRepository
            .fetchAndStoreRemoteTransactions(userUuid = userUuid, nonce = nonce, signature = signature)
            .mapCatching {
                transactionRepository.getLocalTransactions(userUuid)
            }
    }
}