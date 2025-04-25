package com.acme.supermarket.server.domain

import com.acme.supermarket.server.dto.ProductDto
import com.acme.supermarket.server.dto.TransactionDto
import com.acme.supermarket.server.dto.VoucherDto
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    val uuid: String = UUID.randomUUID().toString(),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val totalValue: BigDecimal,

    @Column(nullable = false)
    val accumulatedDiscountUsed: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    val voucherDiscountGenerated: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    val timestamp: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "transaction", cascade = [CascadeType.ALL], orphanRemoval = true)
    val transactionProducts: List<TransactionProduct> = listOf(),

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    val voucherUsed: Voucher? = null
)

fun Transaction.toDto(): TransactionDto {
    return TransactionDto(
        id = this.uuid,
        date = this.timestamp.toString(),
        price = this.totalValue.toDouble(),
        discount = this.accumulatedDiscountUsed.toDouble(),
        products = this.transactionProducts.map { it.product.toDto(it.quantity) },
        voucherUsed = this.voucherUsed?.toDto()
    )
}



