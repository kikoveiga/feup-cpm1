package com.feup.client.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String,
    val date: String,
    val products: List<Product>,
    val price: Double,
    val discount: Double,
    val voucherUsed: Voucher? = null,
)
