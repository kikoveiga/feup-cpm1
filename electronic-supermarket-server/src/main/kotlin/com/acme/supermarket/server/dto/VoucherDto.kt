package com.acme.supermarket.server.dto

data class VoucherDto(
    val uuid: String,
    val userUuid: String,
    val used: Boolean
)
