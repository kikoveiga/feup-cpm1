package com.feup.client.data.repository

import com.feup.client.data.local.database.dao.VoucherDao
import com.feup.client.data.local.database.entity.VoucherEntity
import com.feup.client.data.mapper.toDomain
import com.feup.client.data.remote.SupermarketApi
import com.feup.client.domain.model.Voucher
import com.feup.client.domain.repository.VoucherRepository
import javax.inject.Inject

class VoucherRepositoryImpl @Inject constructor(
    private val api: SupermarketApi,
    private val voucherDao: VoucherDao
) : VoucherRepository {

    override suspend fun getLocalVouchers(userUuid: String): List<Voucher> {
        return voucherDao.getUnusedVouchers(userUuid).map {
            it.toDomain()
        }
    }

    override suspend fun fetchAndStoreVouchers(userUuid: String, nonce: String, signature: String): Result<Unit> {
        return runCatching {
            val remoteVouchers = api.getVouchers(userUuid = userUuid, nonce = nonce, signature = signature)
            val localVouchers = remoteVouchers.map {
                VoucherEntity(
                    voucherUuid = it,
                    userUuid = userUuid
                )
            }

            voucherDao.insertVouchers(localVouchers)
        }
    }
}