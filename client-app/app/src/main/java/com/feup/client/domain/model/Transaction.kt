package com.feup.client.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val userUuid: String,
    val date: String,
    val products: List<Product>,
    val voucherUuid : String? = null,
    val price: Double? = null,
    val useAccumulatedDiscount: Boolean = false,
    val discount: Double = 0.0,
    val signature: String = "",
)
