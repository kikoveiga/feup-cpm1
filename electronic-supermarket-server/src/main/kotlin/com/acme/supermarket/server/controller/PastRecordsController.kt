package com.acme.supermarket.server.controller

import com.acme.supermarket.server.dto.*
import com.acme.supermarket.server.service.PastRecordsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.math.sign

@RestController
@RequestMapping("/api")
class PastRecordsController(
    private val pastRecordsService: PastRecordsService
) {

    @GetMapping("/transactions")
    fun verifyNonceAndGetTransactions(
        @RequestParam userUuid: String,
        @RequestParam nonce: String,
        @RequestParam signature: String
    ): ResponseEntity<List<TransactionDto>> {
        val request = PastRecordsRequestDto(userUuid, nonce, signature)
        val response = pastRecordsService.verifyAndFetchTransactions(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/vouchers")
    fun verifyNonceAndGetVouchers(
        @RequestParam userUuid: String,
        @RequestParam nonce: String,
        @RequestParam signature: String
    ): ResponseEntity<List<String>> {
        val request = PastRecordsRequestDto(userUuid, nonce, signature)
        val response = pastRecordsService.verifyAndFetchVouchers(request)
        return ResponseEntity.ok(response)
    }

}
