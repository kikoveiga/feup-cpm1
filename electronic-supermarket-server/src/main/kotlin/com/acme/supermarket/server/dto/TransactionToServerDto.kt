package com.acme.supermarket.server.dto

data class TransactionToServerDto(
    val userUuid: String,
    val date: String,
    val products: List<ProductDto>,
    val voucherId: String?,
    val useAccumulatedDiscount: Boolean,
    val signature: String
)
