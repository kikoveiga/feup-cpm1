package com.feup.jtp.checkout_terminal.data.model.dto

import java.math.BigDecimal

data class TransactionFromServerDto(
    val isSuccess: Boolean,
    val totalPaid: BigDecimal,
    val totalAccDiscount: BigDecimal,
    val message: String?
)
