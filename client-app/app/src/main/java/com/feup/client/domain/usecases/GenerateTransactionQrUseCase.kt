package com.feup.client.domain.usecases

import com.feup.client.data.dto.TransactionToServerDto
import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.Product
import com.feup.client.domain.model.Transaction
import com.feup.client.domain.model.Voucher
import kotlinx.serialization.json.Json
import javax.inject.Inject


class GenerateTransactionQrUseCase @Inject constructor(
    private val cryptoManager: CryptoManager
) {
    fun invoke(
        userUuid: String,
        products: List<Product>,
        useAccumulatedDiscount: Boolean,
        voucherId: String?
    ): TransactionToServerDto {
        val date = System.currentTimeMillis().toString()

        val signature = cryptoManager.generateSignature(userUuid,
            "$userUuid|$date|${products.hashCode()}|$voucherId|$useAccumulatedDiscount".toByteArray()
        )

        return TransactionToServerDto(
            userUuid = userUuid,
            date = date,
            products = products,
            voucherId = voucherId,
            useAccumulatedDiscount = useAccumulatedDiscount,
            signature = signature
        )
    }

    fun toQrContent(dto: TransactionToServerDto): String {
        val jsonString = Json.encodeToString(dto)
        val jsonBytes = jsonString.toByteArray()
        return cryptoManager.encodeToBase64(jsonBytes)
    }
}
