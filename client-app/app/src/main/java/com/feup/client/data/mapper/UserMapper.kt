package com.feup.client.data.mapper

import com.feup.client.data.local.SerializableUser
import com.feup.client.data.model.dto.PaymentCardDto
import com.feup.client.data.model.dto.RegisterUserRequestDto
import com.feup.client.domain.model.PaymentCard
import com.feup.client.domain.model.PaymentCardType
import com.feup.client.domain.model.User
import java.security.KeyFactory
import java.security.KeyPair
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

fun User.toRegisterUserRequestDto(): RegisterUserRequestDto =
    RegisterUserRequestDto(
        name = name,
        nickname = nickname,
        rsaPublicKey = rsaKeyPair.public.toString(),
        ecPublicKey = ecKeyPair.public.toString(),
        paymentCardDto = paymentCard.toPaymentCardDto()
    )

@OptIn(ExperimentalEncodingApi::class)
fun User.toSerializable(): SerializableUser =
    SerializableUser(
        name = name,
        nickname = nickname,
        rsaPublicKey = Base64.encode(rsaKeyPair.public.encoded),
        rsaPrivateKey = Base64.encode(rsaKeyPair.private.encoded),
        ecPublicKey = Base64.encode(ecKeyPair.public.encoded),
        ecPrivateKey = Base64.encode(ecKeyPair.private.encoded),
        paymentCardType = paymentCard.type.toString(),
        paymentCardNumber = paymentCard.number,
        paymentCardExpirationDate = paymentCard.expirationDate,
        uuid = uuid ?: "",
        supermarketRsaPublicKey = supermarketRsaPublicKey ?: ""
    )

@OptIn(ExperimentalEncodingApi::class)
fun SerializableUser.toUser(): User =
    User(
        name = name,
        nickname = nickname,
        rsaKeyPair = KeyPair(
            KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(Base64.decode(rsaPublicKey))),
            KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(Base64.decode(rsaPrivateKey)))
        ),
        ecKeyPair = KeyPair(
            KeyFactory.getInstance("EC").generatePublic(X509EncodedKeySpec(Base64.decode(ecPublicKey))),
            KeyFactory.getInstance("EC").generatePrivate(PKCS8EncodedKeySpec(Base64.decode(ecPrivateKey)))
        ),
        paymentCard = PaymentCard(enumValues<PaymentCardType>().firstOrNull { it.name == paymentCardType} ?: PaymentCardType.DEBIT, paymentCardNumber, paymentCardExpirationDate),
        uuid = uuid,
        supermarketRsaPublicKey = supermarketRsaPublicKey
    )

fun PaymentCard.toPaymentCardDto(): PaymentCardDto =
    PaymentCardDto(
        type = type.toString(),
        number = number,
        expirationDate = expirationDate
    )
