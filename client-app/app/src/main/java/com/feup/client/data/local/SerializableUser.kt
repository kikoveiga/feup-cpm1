package com.feup.client.data.local

import kotlinx.serialization.Serializable

@Serializable
data class SerializableUser (
    val uuid: String,
    val name: String,
    val nickname: String,
    val rsaPublicKey: String,
    val rsaPrivateKey: String,
    val ecPublicKey: String,
    val ecPrivateKey: String,
    val paymentCardType: String,
    val paymentCardNumber: String,
    val paymentCardExpirationDate: String
)