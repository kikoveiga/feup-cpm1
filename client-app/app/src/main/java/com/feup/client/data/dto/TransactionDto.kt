package com.feup.client.data.dto

data class TransactionDto(
    val transactionUuid: String,
    val date: String,
    val products: List<ProductDto>,
    val price: Double,
    val discount: Double,
    val voucherUuidUsed: String? = null
)
