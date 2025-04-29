package com.feup.terminal.domain.usecases

import com.feup.terminal.domain.crypto.CryptoManager
import com.feup.terminal.domain.model.Transaction
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ScanTransactionUseCase @Inject constructor(
    private val cryptoManager: CryptoManager
) {
    fun invoke(data: String): Result<Transaction> {
        return try {
            val decodedBytes = cryptoManager.decodeFromBase64(data)
            val jsonString = String(decodedBytes)

            val product = Json.decodeFromString<Transaction>(jsonString)
            Result.success(product)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
