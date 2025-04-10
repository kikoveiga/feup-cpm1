package com.feup.jtp.client_app.domain.model

data class Transaction(
    val id: String,
    val user: User,
    val products: List<Product>,
    val voucherUser: Voucher? = null,
)
