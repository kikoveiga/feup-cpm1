package com.feup.client.data.dto

import com.feup.client.domain.model.Product

data class TransactionToServerDto(
    val userUuid: String,
    val date: String,
    val products: List<Product>,
    val voucherId: String?,
    val useAccumulatedDiscount: Boolean,
    val signature: String
)