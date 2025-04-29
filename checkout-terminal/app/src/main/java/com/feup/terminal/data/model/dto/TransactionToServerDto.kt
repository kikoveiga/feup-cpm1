package com.feup.terminal.data.model.dto

data class TransactionToServerDto(
    val userUuid: String,
    val date: String,
    val products: List<ProductDto>,
    val voucherId: String?,
    val useAccumulatedDiscount: Boolean,
    val signature: String
)

