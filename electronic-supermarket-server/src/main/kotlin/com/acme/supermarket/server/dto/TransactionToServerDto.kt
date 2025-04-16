package com.acme.supermarket.server.dto

data class TransactionToServerDto(
    val userUuid: String,
    val items: List<ItemDto>,
    val voucherId: String?,
    val useAccumulatedDiscount: Boolean,
    val signature: String
)
