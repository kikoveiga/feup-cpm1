package com.feup.terminal.data.model.dto

import java.math.BigDecimal

data class TransactionFromServerDto(
    val isSuccess: Boolean,
    val totalPaid: BigDecimal,
    val totalAccDiscount: BigDecimal,
    val message: String?
)
