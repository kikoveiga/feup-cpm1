package com.feup.terminal.data.dto

import java.math.BigDecimal

data class TransactionFromServerDto(
    val isSuccess: Boolean,
    val totalPaid: BigDecimal,
    val totalAccDiscount: BigDecimal,
    val isVoucherCreated: Boolean = false,
    val message: String?
)
