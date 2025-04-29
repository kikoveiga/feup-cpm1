package com.feup.client.domain.usecases

import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.Product
import com.feup.client.domain.model.Transaction
import com.feup.client.domain.model.Voucher
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GenerateTransactionQrUseCase @Inject constructor(
    private val cryptoManager: CryptoManager
) {

    fun invoke(userUuid: String, products: List<Product>, discount: Double = 0.0, voucher: Voucher? = null): Transaction {

        val totalPrice = products.sumOf { it.price * it.quantity } - discount
        return Transaction(
            userUuid = userUuid,
            date = System.currentTimeMillis().toString(),
            products = products,
            price = totalPrice,
            discount = discount,
            voucherUsed = voucher
        )
    }

    fun toQrContent(transaction: Transaction): String {
        val jsonString = Json.encodeToString(transaction)
        val jsonBytes = jsonString.toByteArray()
        return cryptoManager.encodeToBase64(jsonBytes)
    }
}