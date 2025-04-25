package com.acme.supermarket.server.domain

import jakarta.persistence.*

@Entity
@Table(name = "transaction_products")
data class TransactionProduct(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    val transaction: Transaction,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @Column(nullable = false)
    val quantity: Int
)
