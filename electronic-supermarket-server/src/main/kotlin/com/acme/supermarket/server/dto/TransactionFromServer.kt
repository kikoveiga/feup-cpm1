package com.acme.supermarket.server.dto

data class TransactionFromServer(
    val isSuccess: Boolean,
    val totalPaid: Double,
    val totalAccDiscount: Double,
    val message: String?
)
