package com.acme.supermarket.server.dto

data class UserResponseDto(
    val userUuid: String,
    val supermarketRsaPublicKey: String
)
