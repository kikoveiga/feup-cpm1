package com.feup.client.data.mapper

import com.feup.client.data.local.SerializableUser
import com.feup.client.data.model.dto.PaymentCardDto
import com.feup.client.data.model.dto.RegisterUserRequestDto
import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.PaymentCard
import com.feup.client.domain.model.PaymentCardType
import com.feup.client.domain.model.User
import java.security.KeyPair

fun User.toRegisterUserRequestDto(cryptoManager: CryptoManager): RegisterUserRequestDto =
    RegisterUserRequestDto(
        name = name,
        nickname = nickname,
        rsaPublicKey = cryptoManager.encodePublicKeyToBase64(rsaKeyPair.public),
        ecPublicKey = cryptoManager.encodePublicKeyToBase64(ecKeyPair.public),
        paymentCardDto = paymentCard.toPaymentCardDto()
    )

fun User.toSerializable(cryptoManager: CryptoManager): SerializableUser =
    SerializableUser(
        name = name,
        nickname = nickname,
        rsaPublicKey = cryptoManager.encodePublicKeyToBase64(rsaKeyPair.public),
        ecPublicKey = cryptoManager.encodePublicKeyToBase64(ecKeyPair.public),
        paymentCardType = paymentCard.type.toString(),
        paymentCardNumber = paymentCard.number,
        paymentCardExpirationDate = paymentCard.expirationDate,
        uuid = uuid ?: "",
        supermarketRsaPublicKey = supermarketRsaPublicKey?.let { cryptoManager.encodePublicKeyToBase64(it) } ?: ""
    )

fun SerializableUser.toUser(cryptoManager: CryptoManager): User =
    User(
        name = name,
        nickname = nickname,
        rsaKeyPair = KeyPair(
            cryptoManager.decodePublicKeyFromBase64(rsaPublicKey, "RSA"),
            cryptoManager.getPrivateKey(cryptoManager.rsaAlias)
        ),
        ecKeyPair = KeyPair(
            cryptoManager.decodePublicKeyFromBase64(ecPublicKey, "EC"),
            cryptoManager.getPrivateKey(cryptoManager.ecAlias)
        ),
        paymentCard = PaymentCard(enumValues<PaymentCardType>().firstOrNull { it.name == paymentCardType} ?: PaymentCardType.DEBIT, paymentCardNumber, paymentCardExpirationDate),
        uuid = uuid,
        supermarketRsaPublicKey = cryptoManager.decodePublicKeyFromBase64(supermarketRsaPublicKey, "RSA")
    )

fun PaymentCard.toPaymentCardDto(): PaymentCardDto =
    PaymentCardDto(
        type = type.toString(),
        number = number,
        expirationDate = expirationDate
    )
