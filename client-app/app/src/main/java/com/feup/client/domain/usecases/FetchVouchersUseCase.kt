package com.feup.client.domain.usecases

import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.Voucher
import com.feup.client.domain.repository.VoucherRepository
import java.util.UUID
import javax.inject.Inject

class FetchVouchersUseCase @Inject constructor(
    private val voucherRepository: VoucherRepository,
    private val cryptoManager: CryptoManager
) {
    suspend operator fun invoke(userNickname: String, userUuid: String): Result<List<Voucher>> {
        val nonce = UUID.randomUUID().toString().replace("-", "")
        val message = "userUuid:$userUuid&nonce:$nonce"
        val signature = cryptoManager.generateSignature(userNickname = userNickname, message.toByteArray())

        return runCatching {
            voucherRepository.fetchAndStoreVouchers(userUuid = userUuid, nonce = nonce, signature = signature)
            voucherRepository.getLocalVouchers(userUuid)
        }
    }
}