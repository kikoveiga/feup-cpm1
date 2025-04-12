package com.feup.jtp.client_app.domain.model

data class PaymentCard(
    val id: String,
    val type: String,
    val number: String,
    val expirationDate: String
)
