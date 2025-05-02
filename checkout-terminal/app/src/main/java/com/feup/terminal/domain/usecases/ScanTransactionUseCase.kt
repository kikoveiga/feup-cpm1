package com.feup.terminal.domain.usecases

import com.feup.terminal.data.dto.TransactionToServerDto
import com.feup.terminal.domain.crypto.CryptoManager
import com.feup.terminal.domain.model.Transaction
import kotlinx.serialization.json.Json
import javax.inject.Inject


class ScanTransactionUseCase @Inject constructor(
    private val cryptoManager: CryptoManager
) {
    fun invoke(data: String): Result<TransactionToServerDto> {
        return try {
            val decodedBytes = cryptoManager.decodeFromBase64(data)
            val jsonString = String(decodedBytes)

            val json = Json { ignoreUnknownKeys = true }
            val transactionDto = json.decodeFromString<TransactionToServerDto>(jsonString)

            Result.success(transactionDto)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
