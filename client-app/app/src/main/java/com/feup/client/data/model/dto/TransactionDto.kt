package com.feup.client.data.model.dto

data class TransactionDto(
    val id: String,
    val date: String,
    val products: List<ProductDto>,
    val price: Double,
    val discount: Double,
    val voucherUsed: VoucherDto? = null
)
