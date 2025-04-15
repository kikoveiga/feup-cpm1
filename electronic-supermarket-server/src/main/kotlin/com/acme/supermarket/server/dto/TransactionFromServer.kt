package com.feup.jtp.checkout_terminal.data.model.dto

data class TransactionFromServer(
    val isSuccess: Boolean,
    val totalPaid: Double,
    val totalAccDiscount: Double, // Total accumulated discount value stored on the server
)
