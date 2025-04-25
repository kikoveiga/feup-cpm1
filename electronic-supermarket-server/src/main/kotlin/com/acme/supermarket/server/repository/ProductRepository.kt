package com.acme.supermarket.server.repository

import com.acme.supermarket.server.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, String>
