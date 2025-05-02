package com.feup.client.domain.usecases

import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.repository.AccumulatedDiscountRepository
import java.math.BigDecimal
import javax.inject.Inject
import java.util.UUID

class FetchAccumulatedDiscountUseCase @Inject constructor(
    private val accumulatedDiscountRepository: AccumulatedDiscountRepository,
    private val cryptoManager: CryptoManager
) {
    suspend operator fun invoke(userNickname: String, userUuid: String): Result<Result<BigDecimal>> {
        val nonce = UUID.randomUUID().toString().replace("-", "")
        val message = "userUuid:$userUuid&nonce:$nonce"
        val signature = cryptoManager.generateSignature(userNickname = userNickname, message.toByteArray())

        return runCatching {
            accumulatedDiscountRepository.fetchAccumulatedDiscount(
                userUuid = userUuid,
                nonce = nonce,
                signature = signature
            )
        }
    }
}
