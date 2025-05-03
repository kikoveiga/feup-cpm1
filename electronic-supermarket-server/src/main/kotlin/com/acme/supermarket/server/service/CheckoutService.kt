package com.acme.supermarket.server.service

import com.acme.supermarket.server.domain.Product
import com.acme.supermarket.server.domain.Transaction
import com.acme.supermarket.server.domain.TransactionProduct
import com.acme.supermarket.server.domain.Voucher
import com.acme.supermarket.server.dto.ProductDto
import com.acme.supermarket.server.dto.TransactionFromServerDto
import com.acme.supermarket.server.dto.TransactionToServerDto
import com.acme.supermarket.server.repository.*
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class CheckoutService(
    private val userRepository: UserRepository,
    private val voucherRepository: VoucherRepository,
    private val transactionRepository: TransactionRepository,
    private val userService: UserService,
    private val productRepository: ProductRepository,
    private val transactionProductRepository: TransactionProductRepository,
    private val cryptoService: CryptoService
) {

    fun processCheckout(transaction: TransactionToServerDto): TransactionFromServerDto {

        val user = userRepository.findByUserUuid(transaction.userUuid)
            ?: throw BadRequestException("User not found for the given UUID.")


        val messageToVerify = "userUuid:${transaction.userUuid}&nonce:${transaction.date}"

        val isValid = cryptoService.verifyEcSignature(
            publicKeyBase64 = user.ecPublicKey,
            message = messageToVerify,
            signatureBase64 = transaction.signature
        )

        if (!isValid) {
            throw BadRequestException("Invalid signature.")
        }

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
        if (transaction.voucherUuid != null) {
            voucher = voucherRepository.findByUuid(transaction.voucherUuid)

            if (voucher != null && voucher.user.userUuid == transaction.userUuid && !voucher.used) {
                voucherDiscount = totalValue.multiply(BigDecimal(0.15))
                accumulatedDiscount = accumulatedDiscount.add(voucherDiscount)
                voucherRepository.markVoucherAsUsed(voucher.uuid)

            } else {
                return TransactionFromServerDto(
                    isSuccess = false,
                    totalPaid = BigDecimal.ZERO,
                    totalAccDiscount = BigDecimal.ZERO,
                    message = "Invalid voucher, used or does not belong to the user."
                )
            }
        }

        val newTotalSpent = totalValue.add(user.totalSpent)
        val isVoucherCreated = generateVouchersIfEligible(transaction.userUuid,newTotalSpent)

        userService.updateUserHistory(transaction.userUuid, newTotalSpent, accumulatedDiscount)
        val requestDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(transaction.date.toLong()),
            ZoneId.systemDefault()
        )
        val savedTransaction = transactionRepository.save(
            Transaction(
                user = user,
                totalValue = totalValue,
                accumulatedDiscountUsed = if (transaction.useAccumulatedDiscount) accumulatedDiscountUsed else BigDecimal.ZERO,
                voucherDiscountGenerated = voucherDiscount,
                voucherUsed = voucher,
                timestamp = requestDate
            )
        )

        transaction.products.forEach { dto ->
            var product = productRepository.findById(dto.productUuid).orElse(null)
            if (product == null) {
                product = productRepository.save(
                    Product(
                        productUuid = dto.productUuid,
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
            isVoucherCreated = isVoucherCreated,
            message = "Success"
        )
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

    private fun generateVouchersIfEligible(userId: String, newTotal: BigDecimal): Boolean {
        val user = userRepository.findByUserUuid(userId) ?: return false

        val oldTotal = user.totalSpent
        val oldVouchers = oldTotal.divide(BigDecimal(100), 0, RoundingMode.DOWN).toInt()
        val newVouchers = newTotal.divide(BigDecimal(100), 0, RoundingMode.DOWN).toInt()
        val vouchersToGenerate = newVouchers - oldVouchers

        if (vouchersToGenerate <= 0) return false

        val vouchers = mutableListOf<Voucher>()
        repeat(vouchersToGenerate) {
            val voucher = Voucher(
                uuid = UUID.randomUUID().toString(),
                user = user,
                used = false
            )
            vouchers.add(voucherRepository.save(voucher))
        }

        return true
    }

    private fun validateRequest(transaction: TransactionToServerDto) {
        //If the requests are blank
        if (transaction.userUuid.isBlank()) throw BadRequestException("User UUID cannot be empty.")
        if (transaction.products.isEmpty()) throw BadRequestException("Transaction must contain at least one item.")
        transaction.products.forEachIndexed { index, product ->
            if (product.productUuid.isBlank()) {
                throw BadRequestException("Item at index $index has an empty productId.")
            }
            if (product.price <= 0.0) {
                throw BadRequestException("Item at index $index has an invalid price. Must be greater than 0.")
            }
        }
        val totalQuantity = transaction.products.sumOf { it.quantity }
        if (totalQuantity > 10) throw BadRequestException("You can only purchase up to 10 items in total per transaction.")
        if (transaction.signature.isBlank()) throw BadRequestException("Signature cannot be empty.")

        val requestDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(transaction.date.toLong()),
            ZoneId.systemDefault()
        )
        val existingTransactions = transactionRepository.findByUserUserUuidAndTimestamp(transaction.userUuid, requestDate)
        if (existingTransactions != null) {
            if (existingTransactions.isNotEmpty()) {
                throw BadRequestException("This transaction has already been processed.")
            }
        }

    }

}