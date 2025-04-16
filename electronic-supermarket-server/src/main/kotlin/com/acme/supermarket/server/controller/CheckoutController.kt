package com.acme.supermarket.server.controller

import com.acme.supermarket.server.service.CheckoutService
import com.acme.supermarket.server.dto.TransactionFromServerDto
import com.acme.supermarket.server.dto.TransactionToServerDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class CheckoutController(private val checkoutService: CheckoutService) {

    @PostMapping("/checkout")
    fun processCheckout(@RequestBody transaction: TransactionToServerDto): TransactionFromServerDto {
        val result = checkoutService.processCheckout(transaction)
        return result
    }
}
