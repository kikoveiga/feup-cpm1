package com.acme.supermarket.server.dto

data class AuthResponseDto(
    val pastTransactions: List<TransactionDto>,
    val unusedVouchers: List<VoucherDto>
)