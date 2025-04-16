package com.acme.supermarket.server.domain

import jakarta.persistence.*

@Entity
@Table(name = "vouchers")
data class Voucher(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val uuid: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val used: Boolean = false
)