package com.acme.supermarket.server.dto

data class PastRecordsRequestDto(
    val userUuid: String,
    val signedNonce: String = ""
)
