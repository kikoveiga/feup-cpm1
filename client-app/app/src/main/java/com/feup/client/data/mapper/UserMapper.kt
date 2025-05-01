package com.feup.client.data.mapper

import com.feup.client.data.local.datastore.SerializableUser
import com.feup.client.data.dto.PaymentCardDto
import com.feup.client.data.dto.RegisterUserRequestDto
import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.PaymentCard
import com.feup.client.domain.model.PaymentCardType
import com.feup.client.domain.model.User
import java.security.KeyPair

fun User.toRegisterUserRequestDto(cryptoManager: CryptoManager): RegisterUserRequestDto =
    RegisterUserRequestDto(
        name = name,
        nickname = nickname,
        rsaPublicKey = cryptoManager.encodeToBase64(rsaKeyPair.public.encoded),
        ecPublicKey = cryptoManager.encodeToBase64(ecKeyPair.public.encoded),
        paymentCardDto = paymentCard.toPaymentCardDto()
    )

fun User.toSerializable(cryptoManager: CryptoManager): SerializableUser =
    SerializableUser(
        name = name,
        nickname = nickname,
        passwordHash = cryptoManager.encodeToBase64(passwordHash),
        rsaPublicKey = cryptoManager.encodeToBase64(rsaKeyPair.public.encoded),
        ecPublicKey = cryptoManager.encodeToBase64(ecKeyPair.public.encoded),
        paymentCardType = paymentCard.type.toString(),
        paymentCardNumber = paymentCard.number,
        paymentCardExpirationDate = paymentCard.expirationDate,
        uuid = uuid ?: "",
        supermarketRsaPublicKey = supermarketRsaPublicKey?.let { cryptoManager.encodeToBase64(it.encoded) } ?: ""
    )

fun SerializableUser.toUser(cryptoManager: CryptoManager): User =
    User(
        name = name,
        nickname = nickname,
        passwordHash = cryptoManager.decodeFromBase64(passwordHash),
        rsaKeyPair = KeyPair(
            cryptoManager.decodePublicKeyFromBase64(rsaPublicKey, "RSA"),
            cryptoManager.getPrivateKey(userNickname = nickname, isRsa = true)
        ),
        ecKeyPair = KeyPair(
            cryptoManager.decodePublicKeyFromBase64(ecPublicKey, "EC"),
            cryptoManager.getPrivateKey(userNickname = nickname, isRsa = false)
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
