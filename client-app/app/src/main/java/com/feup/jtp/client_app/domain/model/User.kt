package com.feup.jtp.client_app.domain.model

import java.security.KeyPair

data class User(
    val uuid: String? = null,
    val name: String,
    val nickname: String,
    val paymentCard: PaymentCard,
    val rsaKeyPair: KeyPair,
    val ecKeyPair: KeyPair,
)
