package com.feup.terminal.data.dto

import com.feup.terminal.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class TransactionToServerDto(
    val userUuid: String,
    val date: String,
    val products: List<Product>,
    val voucherId: String?,
    val useAccumulatedDiscount: Boolean,
    val signature: String
)

