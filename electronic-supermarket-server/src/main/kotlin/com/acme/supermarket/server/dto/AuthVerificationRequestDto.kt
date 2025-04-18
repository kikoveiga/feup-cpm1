package com.acme.supermarket.server.dto

data class AuthVerificationRequestDto(
    val userUuid: String,
    val signedNonce: String // Base64 encoded
)
