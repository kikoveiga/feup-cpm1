package com.acme.supermarket.server.domain

enum class PaymentCardType(private val displayName: String) {
    DEBIT("Debit"),
    CREDIT("Credit");

    override fun toString(): String = displayName
}