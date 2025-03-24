package com.acme.supermarket.server.dto

data class RegistrationRequest(
    val name: String,
    val rsaPublicKey: String,
    val ecdsaPublicKey: String
)
