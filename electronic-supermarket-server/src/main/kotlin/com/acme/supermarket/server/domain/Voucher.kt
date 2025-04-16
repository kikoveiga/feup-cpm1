package com.acme.supermarket.server.domain

import jakarta.persistence.*

@Entity
@Table(name = "vouchers")
data class Voucher(
    @Id
    val uuid: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    var used: Boolean = false
)