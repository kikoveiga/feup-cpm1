package com.feup.client.data.local

import kotlinx.serialization.Serializable

@Serializable
data class SerializableUser (
    val name: String,
    val nickname: String,
    val passwordHash: String,
    val rsaPublicKey: String,
    val ecPublicKey: String,
    val paymentCardType: String,
    val paymentCardNumber: String,
    val paymentCardExpirationDate: String,
    val uuid: String,
    val supermarketRsaPublicKey: String
)