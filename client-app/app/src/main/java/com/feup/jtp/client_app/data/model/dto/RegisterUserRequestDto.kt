package com.feup.jtp.client_app.data.model.dto

data class RegisterUserRequestDto(
    val name: String,
    val nickname: String,
    val paymentCard: PaymentCardDto,
    val publicRSAKey: String,
    val publicECKey: String
)
