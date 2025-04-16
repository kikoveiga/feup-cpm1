package com.feup.client.domain.model

import java.security.KeyPair

data class User(
    val uuid: String? = null,
    val name: String,
    val nickname: String,
    val rsaKeyPair: KeyPair,
    val ecKeyPair: KeyPair,
    val paymentCard: PaymentCard
)
