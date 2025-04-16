package com.feup.jtp.checkout_terminal.data.model.dto

data class TransactionToServer(
    val userId: String,
    val items: List<ItemDTO>,
    val voucherId: String?,
    val useAccumulatedDiscount: Boolean,
    val signature: String
)

data class ItemDTO(
    val productId: String,
    val price: Double
)