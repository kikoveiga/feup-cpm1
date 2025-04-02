package com.acme.supermarket.server.dto

data class UserResponse(
    val userUuid: String,
    val supermarketRsaPublicKey: String
)
