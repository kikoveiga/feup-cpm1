package com.acme.supermarket.server.dto

data class NonceResponseDto(
    val nonce: String // Base64 encoded
)