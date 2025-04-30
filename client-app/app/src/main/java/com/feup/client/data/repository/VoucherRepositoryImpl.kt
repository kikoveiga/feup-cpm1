package com.feup.client.data.repository

import com.feup.client.data.local.database.dao.VoucherDao
import com.feup.client.data.mapper.toDomain
import com.feup.client.data.mapper.toEntity
import com.feup.client.data.remote.SupermarketApi
import com.feup.client.domain.model.Voucher
import com.feup.client.domain.repository.VoucherRepository
import javax.inject.Inject

class VoucherRepositoryImpl @Inject constructor(
    private val api: SupermarketApi,
    private val voucherDao: VoucherDao
) : VoucherRepository {

    override fun getLocalVouchers(userUuid: String): List<Voucher> {
        return voucherDao.getUnusedVouchers(userUuid).map {
            it.toDomain()
        }
    }

    override suspend fun fetchAndStoreVouchers(userUuid: String): Result<Unit> {
        return runCatching {
            val remoteVouchers = api.getVouchers(userUuid)
            val localVouchers = remoteVouchers.map {
                it.toEntity(userUuid)
            }
            voucherDao.insertVouchers(localVouchers)
        }
    }
}