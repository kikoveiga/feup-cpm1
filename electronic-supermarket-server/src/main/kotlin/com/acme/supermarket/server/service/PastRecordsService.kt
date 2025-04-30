package com.acme.supermarket.server.service


import com.acme.supermarket.server.domain.toDto
import com.acme.supermarket.server.dto.*
import com.acme.supermarket.server.repository.TransactionRepository
import com.acme.supermarket.server.repository.UserRepository
import com.acme.supermarket.server.repository.VoucherRepository
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Service
class PastRecordsService(
    private val nonceStore: NonceStore,
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
    private val voucherRepository: VoucherRepository,
    private val cryptoService: CryptoService
) {

    fun generateAndStoreNonce(userUuid: String): String {

       userRepository.findByUserUuid(userUuid) ?: throw BadRequestException("User not found")

        val nonceBytes = ByteArray(16)
        SecureRandom().nextBytes(nonceBytes)
        val nonceBase64 = Base64.getEncoder().encodeToString(nonceBytes)

        nonceStore.storeNonce(userUuid, nonceBase64)

        return nonceBase64
    }

    fun verifyAndFetchTransactions(request: PastRecordsRequestDto): List<TransactionDto> {

        val user = userRepository.findByUserUuid(request.userUuid)
            ?: throw BadRequestException("User not found")

        val messageToDecode = request.userUuid + request.signature;

        val isValid = cryptoService.verifyEcSignature(
            publicKeyBase64 = user.ecPublicKey,
            message = messageToDecode,
            signatureBase64 = request.signature
        )

        if (!isValid) {
            throw BadRequestException("Invalid signature.")
        }

        val transactions = transactionRepository.findByUserUserUuid(user.userUuid)

        return transactions.map { it.toDto() }
    }


    fun verifyAndFetchVouchers(request: PastRecordsRequestDto): List<String> {

        val user = userRepository.findByUserUuid(request.userUuid)
            ?: throw BadRequestException("User not found")

        val messageToDecode = request.userUuid + request.signature;

        val isValid = cryptoService.verifyEcSignature(
            publicKeyBase64 = user.ecPublicKey,
            message = messageToDecode,
            signatureBase64 = request.signature
        )

        if (!isValid) {
            throw BadRequestException("Invalid signature.")
        }

        val vouchers = voucherRepository.findByUserAndUsedFalse(user)

        return vouchers.map { it.uuid }
    }

}

