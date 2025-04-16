package com.acme.supermarket.server.dto

data class TransactionToServer(
    val userId: String,
    val items: List<ItemDto>,
    val voucherId: String?,
    val useAccumulatedDiscount: Boolean,
    val signature: String
)
