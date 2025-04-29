package com.acme.supermarket.server.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDto(
    val transactionUuid: String,
    val date: String,
    val products: List<ProductDto>,
    val price: Double,
    val discount: Double,
    val voucherUsed: VoucherDto? = null
)
