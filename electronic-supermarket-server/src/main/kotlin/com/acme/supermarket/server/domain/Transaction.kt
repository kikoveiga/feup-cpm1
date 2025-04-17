package com.acme.supermarket.server.domain

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
    val timestamp: LocalDateTime = LocalDateTime.now()
)

