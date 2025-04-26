package com.feup.client.domain.usecases

import com.feup.client.domain.model.Product
import com.feup.client.domain.model.Transaction
import com.feup.client.domain.model.Voucher
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GenerateTransactionQrUseCase @Inject constructor() {

    fun invoke(products: List<Product>, discount: Double = 0.0, voucher: Voucher? = null): Transaction {

        val totalPrice = products.sumOf { it.price * it.quantity } - discount
        return Transaction(
            id = "transaction-${System.currentTimeMillis()}",
            date = System.currentTimeMillis().toString(),
            products = products,
            price = totalPrice,
            discount = discount,
            voucherUsed = voucher
        )
    }

    fun toQrContent(transaction: Transaction): String {
        return Json.encodeToString(transaction)
    }
}