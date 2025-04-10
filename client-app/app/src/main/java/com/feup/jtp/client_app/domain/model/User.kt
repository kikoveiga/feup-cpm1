package com.feup.jtp.client_app.domain.model

data class User(
    val id: String,
    val name: String,
    val paymentCard: PaymentCard,
    val publicRSAKey: String,
    val publicECKey: String,
)
