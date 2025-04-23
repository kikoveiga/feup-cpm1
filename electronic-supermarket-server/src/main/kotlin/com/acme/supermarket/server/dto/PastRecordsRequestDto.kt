package com.acme.supermarket.server.dto

data class PastRecordsRequestDto(
    val uuid: String,
    val signedNonce: String = ""
)
