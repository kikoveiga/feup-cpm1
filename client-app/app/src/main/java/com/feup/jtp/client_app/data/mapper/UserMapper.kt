package com.feup.jtp.client_app.data.mapper

import com.feup.jtp.client_app.data.model.dto.PaymentCardDto
import com.feup.jtp.client_app.data.model.dto.RegisterUserRequestDto
import com.feup.jtp.client_app.domain.model.PaymentCard
import com.feup.jtp.client_app.domain.model.User

fun User.toRegisterUserRequestDto(): RegisterUserRequestDto =
    RegisterUserRequestDto(
        name = name,
        nickname = nickname,
        paymentCard = paymentCard.toPaymentCardDto(),
        publicRSAKey = publicRSAKey,
        publicECKey = publicECKey
    )

fun PaymentCard.toPaymentCardDto(): PaymentCardDto =
    PaymentCardDto(
        type = type,
        number = number,
        expirationDate = expirationDate
    )