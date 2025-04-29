package com.acme.supermarket.server.domain

import com.acme.supermarket.server.dto.ProductDto
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "products")
data class Product(
    @Id
    val productUuid: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val price: BigDecimal
)

fun Product.toDto(quantity: Int): ProductDto {
    return ProductDto(
        productUuid = this.productUuid,
        name = this.name,
        price = this.price.toDouble(),
        quantity = quantity
    )
}
