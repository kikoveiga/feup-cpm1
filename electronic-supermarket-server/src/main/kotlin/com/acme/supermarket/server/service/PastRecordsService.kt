package com.acme.supermarket.server.service


import com.acme.supermarket.server.domain.toDto
import com.acme.supermarket.server.dto.PastRecordsRequestDto
import com.acme.supermarket.server.dto.TransactionDto
import com.acme.supermarket.server.repository.TransactionRepository
import com.acme.supermarket.server.repository.UserRepository
import com.acme.supermarket.server.repository.VoucherRepository
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PastRecordsService(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
    private val voucherRepository: VoucherRepository,
    private val cryptoService: CryptoService
) {

    fun verifyAndFetchTransactions(request: PastRecordsRequestDto): List<TransactionDto> {

        val user = userRepository.findByUserUuid(request.userUuid)
            ?: throw BadRequestException("User not found")

        val messageToVerify = "userUuid:${request.userUuid}&nonce:${request.nonce}"

        val isValid = cryptoService.verifyEcSignature(
            publicKeyBase64 = user.ecPublicKey,
            message = messageToVerify,
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

        val messageToVerify = "userUuid:${request.userUuid}&nonce:${request.nonce}"

        val isValid = cryptoService.verifyEcSignature(
            publicKeyBase64 = user.ecPublicKey,
            message = messageToVerify,
            signatureBase64 = request.signature
        )

        if (!isValid) {
            throw BadRequestException("Invalid signature.")
        }

        val vouchers = voucherRepository.findByUserAndUsedFalse(user)

        return vouchers.map { it.uuid }
    }

    fun verifyAndCalculateAccumulatedDiscount(request: PastRecordsRequestDto): BigDecimal {

        val user = userRepository.findByUserUuid(request.userUuid)
            ?: throw BadRequestException("User not found")

        val messageToVerify = "userUuid:${request.userUuid}&nonce:${request.nonce}"

        val isValid = cryptoService.verifyEcSignature(
            publicKeyBase64 = user.ecPublicKey,
            message = messageToVerify,
            signatureBase64 = request.signature
        )

        if (!isValid) {
            throw BadRequestException("Invalid signature.")
        }

        return user.accumulatedDiscount
    }
}

