package com.acme.supermarket.server.repository

import com.acme.supermarket.server.domain.TransactionProduct
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionProductRepository : JpaRepository<TransactionProduct, Long>
