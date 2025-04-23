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
    private val voucherRepository: VoucherRepository
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

        val user = userRepository.findByUserUuid(request.uuid)
            ?: throw BadRequestException("User not found")
        /*
        val storedNonce = nonceStore.getNonce(request.uuid)
            ?: throw BadRequestException("No nonce stored")

        val publicKey = getPublicKeyFromString(user.rsaPublicKey)


        val isValid = verifySignature(
            storedNonce.toByteArray(),
            Base64.getDecoder().decode(request.signedNonce),
            publicKey
        )

        if (!isValid) {
            throw BadRequestException("Signature verification failed")
        }
*/
        val transactions = transactionRepository.findByUserUserUuid(user.userUuid)

        return transactions.map { it.toDto() }
    }


    fun verifyAndFetchVouchers(request: PastRecordsRequestDto): List<VoucherDto> {

        val user = userRepository.findByUserUuid(request.uuid)
            ?: throw BadRequestException("User not found")
        /*
        val storedNonce = nonceStore.getNonce(request.uuid)
            ?: throw BadRequestException("No nonce stored")

        val publicKey = getPublicKeyFromString(user.rsaPublicKey)


        val isValid = verifySignature(
            storedNonce.toByteArray(),
            Base64.getDecoder().decode(request.signedNonce),
            publicKey
        )

        if (!isValid) {
            throw BadRequestException("Signature verification failed")
        }
*/
        val vouchers = voucherRepository.findByUserAndUsedFalse(user)

        return vouchers.map { it.toDto() }
    }

    private fun verifySignature(data: ByteArray, signatureBytes: ByteArray, publicKey: PublicKey): Boolean {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(signatureBytes)
    }

    private fun getPublicKeyFromString(key: String): PublicKey {
        val keyBytes = Base64.getDecoder().decode(key)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }
}

