package com.feup.client.domain.model

import kotlinx.serialization.Serializable

enum class PaymentCardType(private val displayName: String) {
    DEBIT("Debit"),
    CREDIT("Credit");

    override fun toString(): String = displayName
}

@Serializable
data class PaymentCard(
    val type: PaymentCardType,
    val number: String,
    val expirationDate: String
)
