package com.feup.client.data.mapper

import com.feup.client.data.local.database.entity.VoucherEntity
import com.feup.client.domain.model.Voucher

fun VoucherEntity.toDomain(): Voucher =
    Voucher(
        voucherUuid = voucherUuid,
        userUuid = userUuid,
        isUsed = isUsed
    )