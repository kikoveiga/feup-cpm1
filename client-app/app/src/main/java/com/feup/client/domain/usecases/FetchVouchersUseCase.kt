package com.feup.client.domain.usecases

import com.feup.client.domain.model.Voucher
import com.feup.client.domain.repository.VoucherRepository
import javax.inject.Inject

class FetchVouchersUseCase @Inject constructor(
    private val voucherRepository: VoucherRepository
) {
    suspend operator fun invoke(userUuid: String): Result<List<Voucher>> {
        return runCatching {
            voucherRepository.fetchAndStoreVouchers(userUuid)
            voucherRepository.getLocalVouchers(userUuid)
        }
    }
}