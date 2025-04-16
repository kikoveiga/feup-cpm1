package com.feup.jtp.client_app.domain.model

enum class PaymentCardType(private val displayName: String) {
    DEBIT("Debit"),
    CREDIT("Credit");

    override fun toString(): String = displayName
}

data class PaymentCard(
    val type: PaymentCardType,
    val number: String,
    val expirationDate: String
)
