package com.feup.client.data.model.dto

data class RegisterUserRequestDto(
    val name: String,
    val nickname: String,
    val rsaPublicKey: String,
    val ecPublicKey: String,
    val paymentCardDto: PaymentCardDto
)
