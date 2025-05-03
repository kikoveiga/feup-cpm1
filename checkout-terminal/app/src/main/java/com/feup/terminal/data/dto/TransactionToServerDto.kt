package com.feup.terminal.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransactionToServerDto(
    val userUuid: String,
    val date: String,
    val products: List<ProductDto>,
    val voucherUuid: String? = null,
    val useAccumulatedDiscount: Boolean = false,
    val signature: String
)
