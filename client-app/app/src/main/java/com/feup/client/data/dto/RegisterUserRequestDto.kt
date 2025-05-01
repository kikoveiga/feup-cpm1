package com.feup.client.data.dto

data class RegisterUserRequestDto(
    val name: String,
    val nickname: String,
    val rsaPublicKey: String,
    val ecPublicKey: String,
    val paymentCardDto: PaymentCardDto
)
