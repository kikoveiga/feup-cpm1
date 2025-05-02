package com.feup.client.domain.usecases

import com.feup.client.data.dto.TransactionToServerDto
import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.model.Product
import com.feup.client.domain.model.Transaction
import com.feup.client.domain.model.Voucher
import kotlinx.serialization.json.Json
import javax.inject.Inject


class GenerateTransactionQrUseCase @Inject constructor(
    private val cryptoManager: CryptoManager,
    private val userDataStore: UserDataStore
) {
    suspend fun invoke(
        userUuid: String,
        products: List<Product>,
        useAccumulatedDiscount: Boolean,
        voucherId: String?
    ): TransactionToServerDto {
        val date = System.currentTimeMillis().toString()
        val nickname = userDataStore.getLoggedInUser().nickname
        val signature = cryptoManager.generateSignature(
            userNickname = nickname,
            "asdasd".toByteArray()
        )
        return TransactionToServerDto(
            userUuid = userUuid,
            date = date,
            products = products,
            voucherId = voucherId,
            useAccumulatedDiscount = useAccumulatedDiscount,
            signature = "ZmFrZV9zaWduYXR1cmU="
        )
    }

    fun toQrContent(dto: TransactionToServerDto): String {
        val jsonString = Json.encodeToString(dto)
        val jsonBytes = jsonString.toByteArray()
        return cryptoManager.encodeToBase64(jsonBytes)
    }
}
