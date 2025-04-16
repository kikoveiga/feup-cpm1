package com.acme.supermarket.server.controller

import com.acme.supermarket.server.dto.TransactionToServer
import com.acme.supermarket.server.dto.TransactionFromServer
import org.springframework.web.bind.annotation.*
/*
@RestController
@RequestMapping("/api")
class CheckoutController(private val checkoutService: CheckoutService) {

    @PostMapping("/checkout")
    fun processCheckout(@RequestBody transaction: TransactionToServer): TransactionFromServer {
        val result = checkoutService.processCheckout(transaction)
        return result
    }
}
*/