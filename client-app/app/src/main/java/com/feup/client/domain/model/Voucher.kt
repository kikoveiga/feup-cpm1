package com.feup.client.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Voucher(
    val voucherUuid: String,
    val userUuid: String,
    val isUsed: Boolean
)
