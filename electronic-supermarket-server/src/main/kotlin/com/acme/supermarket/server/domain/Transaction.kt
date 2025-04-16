package com.acme.supermarket.server.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
open class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val amountPaid: Double,

    @Column(nullable = false)
    val transactionDate: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val voucherUsed: Boolean = false,

    @Column(nullable = false)
    val accumulatedDiscount: Double = 0.0
)
