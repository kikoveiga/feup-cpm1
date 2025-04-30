package com.feup.client.domain.repository

import com.feup.client.domain.model.Voucher

interface VoucherRepository {
    fun getLocalVouchers(userUuid: String): List<Voucher>
    suspend fun fetchAndStoreVouchers(userUuid: String): Result<Unit>
}