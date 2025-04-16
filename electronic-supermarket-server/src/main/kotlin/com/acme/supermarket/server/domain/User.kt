package com.acme.supermarket.server.domain

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "users")
open class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val userUuid: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val nickname: String,

    @Column(nullable = false)
    val rsaPublicKey: String,

    @Column(nullable = false)
    val ecPublicKey: String,

    @Column(nullable = false)
    val cardType: String,

    @Column(nullable = false)
    val cardNumber: String,

    @Column(nullable = false)
    val cardExpirationDate: String,

    @Column(nullable = false)
    var accumulatedDiscount: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var totalSpent: BigDecimal = BigDecimal.ZERO,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val vouchers: List<Voucher> = emptyList()
)
