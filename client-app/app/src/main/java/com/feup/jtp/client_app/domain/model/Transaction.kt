package com.feup.jtp.client_app.domain.model

data class Transaction(
    val id: String,
    val date: String,
    val products: List<Product>,
    val price: Double,
    val discount: Double,
    val voucherUsed: Voucher? = null,
)
