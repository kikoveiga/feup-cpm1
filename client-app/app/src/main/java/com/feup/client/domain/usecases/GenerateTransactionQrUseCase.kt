package com.feup.client.domain.usecases

import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.Product
import com.feup.client.domain.model.Transaction
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GenerateTransactionQrUseCase @Inject constructor(
    private val cryptoManager: CryptoManager,
) {
    fun invoke(
        userUuid: String,
        userNickname: String,
        products: List<Product>,
        useAccumulatedDiscount: Boolean,
        voucherUuid: String?
    ): Transaction {

        val date = System.currentTimeMillis().toString() // acts as a nonce

        val message = "userUuid:$userUuid&nonce:$date"

        val signature = cryptoManager.generateSignature(userNickname = userNickname, message.toByteArray())

        return Transaction(
            userUuid = userUuid,
            date = date,
            products = products,
            voucherUuid = voucherUuid,
            useAccumulatedDiscount = useAccumulatedDiscount,
            signature = signature
        )
    }

    fun toQrContent(transaction: Transaction): String {
        val jsonString = Json.encodeToString(transaction)
        val jsonBytes = jsonString.toByteArray()
        return cryptoManager.encodeToBase64(jsonBytes)
    }
}
