package com.acme.supermarket.server.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDto(
    val uuid: String,
    val userUuid: String,
    val totalValue: BigDecimal,
    val accumulatedDiscountUsed: BigDecimal,
    val voucherDiscountGenerated: BigDecimal,
    val timestamp: LocalDateTime
)
