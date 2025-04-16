package com.feup.jtp.checkout_terminal.data.model.dto

data class TransactionToServerDto(
    val userUuid: String,
    val items: List<ItemDto>,
    val voucherId: String?,
    val useAccumulatedDiscount: Boolean,
    val signature: String
)
