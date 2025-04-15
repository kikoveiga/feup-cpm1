package com.acme.supermarket.server.dto

data class PaymentCardDto(
    val type: String,
    val number: String,
    val expirationDate: String,
)