package com.acme.supermarket.server.controller

import com.acme.supermarket.server.dto.PastRecordsRequestDto
import com.acme.supermarket.server.dto.TransactionDto
import com.acme.supermarket.server.service.PastRecordsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

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

    @GetMapping("/accumulated-discount")
    fun getAccumulatedDiscount(
        @RequestParam userUuid: String,
        @RequestParam nonce: String,
        @RequestParam signature: String
    ): ResponseEntity<BigDecimal> {
        val request = PastRecordsRequestDto(userUuid, nonce, signature)
        val accumulatedDiscount = pastRecordsService.verifyAndCalculateAccumulatedDiscount(request)
        return ResponseEntity.ok(accumulatedDiscount)
    }


}
