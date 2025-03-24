package com.acme.supermarket.server.domain

import jakarta.persistence.*

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

    val rsaPublicKey: String,
    val ecdsaPublicKey: String,
)