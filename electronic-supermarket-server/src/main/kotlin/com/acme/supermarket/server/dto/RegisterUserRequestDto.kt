package com.acme.supermarket.server.dto

data class RegisterUserRequestDto(
    val name: String,
    val nickname: String,
    val rsaPublicKey: String,
    val ecPublicKey: String,
    val paymentCardDto: PaymentCardDto
)
