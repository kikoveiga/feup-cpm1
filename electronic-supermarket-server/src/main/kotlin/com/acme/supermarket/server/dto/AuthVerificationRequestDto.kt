package com.acme.supermarket.server.dto

data class AuthVerificationRequestDto(
    val uuid: String,
    val signedNonce: String // Base64 encoded
)
