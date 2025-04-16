package com.acme.supermarket.server.service

import com.acme.supermarket.server.dto.*
import com.acme.supermarket.server.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.*
/*
@Service
class CheckoutService(private val userRepository: UserRepository, private val voucherRepository: VoucherRepository) {

    private val THRESHOLD_AMOUNT = BigDecimal(100.00)

    fun processCheckout(transaction: TransactionToServer): TransactionFromServer {
        if (!verifySignature(transaction)) {
            return TransactionFromServer(false,
                0.0,
                0.0,
                "Signature verification failure."
            )
        }

        var totalValue = calculateTotalValue(transaction.items)
        var accumulatedDiscount = getAccumulatedDiscount(transaction.userId)

        if (transaction.useAccumulatedDiscount) {
            totalValue = totalValue.subtract(accumulatedDiscount)
            accumulatedDiscount = BigDecimal.ZERO
        }

        var voucherDiscount = BigDecimal.ZERO
        if (transaction.voucherId != null) {
            val voucher = voucherRepository.findByIdAndUserId(transaction.voucherId, transaction.userId)
            if (voucher != null) {
                voucherDiscount = totalValue.multiply(BigDecimal(0.15))
                totalValue = totalValue.subtract(voucherDiscount)
                // Adicionar o desconto de voucher ao desconto acumulado no servidor
                accumulatedDiscount = accumulatedDiscount.add(voucherDiscount)
            } else {
                return CheckoutResponse(false, "Voucher inválido ou não pertence ao usuário.")
            }
        }

        // 3. Atualizar o histórico do usuário
        updateUserHistory(transaction.userId, totalValue, accumulatedDiscount)

        // 4. Gerar novos vouchers se necessário
        if (totalValue >= THRESHOLD_AMOUNT) {
            generateVoucherForUser(transaction.userId)
        }

        // 5. Retornar a resposta
        return CheckoutResponse(true, "Pagamento realizado com sucesso!", totalValue, accumulatedDiscount)
    }

    private fun verifySignature(transaction: TransactionToServer): Boolean {
        val user = userRepository.findByUserUuid(transaction.userId) ?: return false
        val publicKey = getPublicKeyFromBase64(user.ecdsaPublicKey)
        val message = generateMessage(transaction)

        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initVerify(publicKey)
        signature.update(message.toByteArray())

        return signature.verify(Base64.getDecoder().decode(transaction.signature))
    }

    private fun generateMessage(transaction: TransactionToServer): String {
        val itemsString = transaction.items.joinToString(",") { "${it.productId}:${it.price}" }
        return "${transaction.userId}|$itemsString|${transaction.voucherId ?: ""}|${transaction.useAccumulatedDiscount}"
    }

    private fun calculateTotalValue(items: List<ItemDto>): BigDecimal {
        return items.fold(BigDecimal.ZERO) { total, item -> total.add(BigDecimal(item.price)) }
    }

    // Recupera o desconto acumulado do usuário
    private fun getAccumulatedDiscount(userId: String): BigDecimal {
        return userRepository.getAccumulatedDiscount(userId) ?: BigDecimal.ZERO
    }

    // Atualiza o histórico de compras do usuário
    private fun updateUserHistory(userId: String, totalValue: BigDecimal, accumulatedDiscount: BigDecimal) {
        userRepository.updateTotalSpent(userId, totalValue)
        userRepository.updateAccumulatedDiscount(userId, accumulatedDiscount)
    }

    // Gera um novo voucher para o usuário
    private fun generateVoucherForUser(userId: String) {
        val newVoucherId = UUID.randomUUID().toString()
        voucherRepository.save(Voucher(userId, newVoucherId))
    }

    private fun getPublicKeyFromBase64(base64Key: String): PublicKey {
        val keyBytes = Base64.getDecoder().decode(base64Key)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePublic(keySpec)
    }
}
*/