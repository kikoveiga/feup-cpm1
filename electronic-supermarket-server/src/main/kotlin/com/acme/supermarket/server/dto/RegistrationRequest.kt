package com.acme.supermarket.server.dto

data class RegistrationRequest(
    val name: String,
    val nickname: String,
    val rsaPublicKey: String,
    val ecdsaPublicKey: String,
    val cardType: String,
    val cardNumber: String,
    val cardExpirationDate: String
)
