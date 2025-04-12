package com.feup.jtp.client_app.domain.model

data class User(
    val uuid: String,
    val name: String,
    val nickname: String,
    val paymentCard: PaymentCard,
    val publicRSAKey: String,
    val publicECKey: String,
)
