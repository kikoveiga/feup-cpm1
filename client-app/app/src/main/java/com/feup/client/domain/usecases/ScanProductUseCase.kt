package com.feup.client.domain.usecases

import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.Product
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ScanProductUseCase @Inject constructor(
    private val cryptoManager: CryptoManager
) {
    fun invoke(encryptedData: String): Result<Product> {
        return try {
            val decodedBytes = cryptoManager.decodeFromBase64(encryptedData)
            val jsonString = String(decodedBytes)

            val product = Json.decodeFromString<Product>(jsonString)
            Result.success(product)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}