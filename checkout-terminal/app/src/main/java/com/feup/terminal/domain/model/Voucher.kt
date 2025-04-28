package com.feup.terminal.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Voucher(
    val id: String,
    val isUsed: Boolean
)