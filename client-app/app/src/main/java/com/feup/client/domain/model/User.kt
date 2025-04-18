package com.feup.client.domain.model

import java.security.KeyPair
import java.security.PublicKey

data class User(
    val name: String,
    val nickname: String,
    val passwordHash: ByteArray,
    val rsaKeyPair: KeyPair,
    val ecKeyPair: KeyPair,
    val paymentCard: PaymentCard,
    val uuid: String? = null,
    val supermarketRsaPublicKey: PublicKey? = null
)
