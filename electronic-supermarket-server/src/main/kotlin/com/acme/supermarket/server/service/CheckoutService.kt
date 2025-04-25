package com.acme.supermarket.server.service

import com.acme.supermarket.server.domain.Product
import com.acme.supermarket.server.domain.Transaction
import com.acme.supermarket.server.domain.TransactionProduct
import java.math.RoundingMode
import com.acme.supermarket.server.domain.Voucher
import com.acme.supermarket.server.dto.*
import com.acme.supermarket.server.repository.*
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Service
class CheckoutService(
    private val userRepository: UserRepository,
    private val voucherRepository: VoucherRepository,
    private val transactionRepository: TransactionRepository,
    private val userService: UserService,
    private val productRepository: ProductRepository,
    private val transactionProductRepository: TransactionProductRepository
) {

    fun processCheckout(transaction: TransactionToServerDto): TransactionFromServerDto {
      /*  if (!verifySignature(transaction)) {
            return TransactionFromServerDto(false,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "Signature verification failure."
            )
        }
*/
        validateRequest(transaction)

        var totalValue = calculateTotalValue(transaction.products)
        var accumulatedDiscount = getAccumulatedDiscount(transaction.userUuid)
        var accumulatedDiscountUsed = BigDecimal.ZERO

        if (transaction.useAccumulatedDiscount) {
            if (totalValue < accumulatedDiscount){
                accumulatedDiscountUsed = totalValue
                accumulatedDiscount = accumulatedDiscount.subtract(totalValue)
                totalValue = BigDecimal.ZERO
            }
            else {
                accumulatedDiscountUsed = accumulatedDiscount
                totalValue = totalValue.subtract(accumulatedDiscount)
                accumulatedDiscount = BigDecimal.ZERO
            }
        }

        var voucherDiscount = BigDecimal.ZERO
        var voucher: Voucher? = null
        if (transaction.voucherId != null) {
            voucher = voucherRepository.findByUuid(transaction.voucherId)

            if (voucher != null && voucher.user.userUuid == transaction.userUuid && !voucher.used) {
                voucherDiscount = totalValue.multiply(BigDecimal(0.15))
                accumulatedDiscount = accumulatedDiscount.add(voucherDiscount)
                voucherRepository.markVoucherAsUsed(voucher.uuid)

            } else {
                return TransactionFromServerDto(
                    false,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    "Invalid voucher, used or does not belong to the user."
                )
            }
        }
        val user = userRepository.findByUserUuid(transaction.userUuid)
        if (user == null) {
            throw BadRequestException("User not found for the given UUID.")
        }

        val newTotalSpent = totalValue.add(user.totalSpent)
        generateVouchersIfEligible(transaction.userUuid,newTotalSpent)

        userService.updateUserHistory(transaction.userUuid, newTotalSpent, accumulatedDiscount)

        val savedTransaction = transactionRepository.save(
            Transaction(
                user = user!!,
                totalValue = totalValue,
                accumulatedDiscountUsed = if (transaction.useAccumulatedDiscount) accumulatedDiscountUsed else BigDecimal.ZERO,
                voucherDiscountGenerated = voucherDiscount,
                voucherUsed = voucher
            )
        )

        transaction.products.forEach { dto ->
            var product = productRepository.findById(dto.id).orElse(null)
            if (product == null) {
                product = productRepository.save(
                    Product(
                        id = dto.id,
                        name = dto.name,
                        price = BigDecimal(dto.price)
                    )
                )
            }
            transactionProductRepository.save(
                TransactionProduct(
                    transaction = savedTransaction,
                    product = product,
                    quantity = dto.quantity
                )
            )
        }
        return TransactionFromServerDto(
            isSuccess = true,
            totalPaid = totalValue.setScale(2, RoundingMode.HALF_UP),
            totalAccDiscount = accumulatedDiscount.setScale(2, RoundingMode.HALF_UP),
            message = "Success"
        )
    }

    private fun verifySignature(transaction: TransactionToServerDto): Boolean {
        val user = userRepository.findByUserUuid(transaction.userUuid) ?: return false
        val publicKey = getPublicKeyFromBase64(user.ecPublicKey)
        val message = generateMessage(transaction)

        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initVerify(publicKey)
        signature.update(message.toByteArray())

        return signature.verify(Base64.getDecoder().decode(transaction.signature))
    }

    private fun generateMessage(transaction: TransactionToServerDto): String {
        val itemsString = transaction.products.joinToString(",") { "${it.id}:${it.price}" }
        return "${transaction.userUuid}|$itemsString|${transaction.voucherId ?: ""}|${transaction.useAccumulatedDiscount}"
    }

    private fun calculateTotalValue(items: List<ProductDto>): BigDecimal {
        return items.fold(BigDecimal.ZERO) { total, product ->
            val itemTotal = BigDecimal(product.price).multiply(BigDecimal(product.quantity))
            total.add(itemTotal)
        }
    }

    private fun getAccumulatedDiscount(userId: String): BigDecimal {
        return userRepository.getAccumulatedDiscount(userId) ?: BigDecimal.ZERO
    }

    private fun generateVouchersIfEligible(userId: String, newTotal: BigDecimal) {
        val user = userRepository.findByUserUuid(userId) ?: return

        val oldTotal = user.totalSpent
        val oldVouchers = oldTotal.divide(BigDecimal(100), 0, RoundingMode.DOWN).toInt()
        val newVouchers = newTotal.divide(BigDecimal(100), 0, RoundingMode.DOWN).toInt()
        val vouchersToGenerate = newVouchers - oldVouchers

        if (vouchersToGenerate <= 0) return

        val vouchers = mutableListOf<Voucher>()
        repeat(vouchersToGenerate) {
            val voucher = Voucher(
                uuid = UUID.randomUUID().toString(),
                user = user,
                used = false
            )
            vouchers.add(voucherRepository.save(voucher))
        }

    }

    private fun getPublicKeyFromBase64(base64Key: String): PublicKey {
        val keyBytes = Base64.getDecoder().decode(base64Key)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePublic(keySpec)
    }

    private fun validateRequest(transaction: TransactionToServerDto) {
        //If the requests are blank
        if (transaction.userUuid.isBlank()) throw BadRequestException("User UUID cannot be empty.")
        if (transaction.products.isEmpty()) throw BadRequestException("Transaction must contain at least one item.")
        transaction.products.forEachIndexed { index, product ->
            if (product.id.isBlank()) {
                throw BadRequestException("Item at index $index has an empty productId.")
            }
            if (product.price <= 0.0) {
                throw BadRequestException("Item at index $index has an invalid price. Must be greater than 0.")
            }
        }
        if (transaction.signature.isBlank()) throw BadRequestException("Signature cannot be empty.")
    }

}
