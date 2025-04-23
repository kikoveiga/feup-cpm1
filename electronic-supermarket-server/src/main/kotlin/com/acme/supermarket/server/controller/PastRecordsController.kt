package com.acme.supermarket.server.controller

import com.acme.supermarket.server.dto.*
import com.acme.supermarket.server.service.PastRecordsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class PastRecordsController(
    private val pastRecordsService: PastRecordsService
) {

    @GetMapping("/nonce")
    fun getNonce(@RequestParam uuid: String): ResponseEntity<NonceResponseDto> {
        val nonce = pastRecordsService.generateAndStoreNonce(uuid)
        return ResponseEntity.ok(NonceResponseDto(nonce))
    }

    @PostMapping("/transactions")
    fun verifyNonceAndGetTransactions(@RequestBody authRequest: PastRecordsRequestDto): ResponseEntity<List<TransactionDto>> {
        val response = pastRecordsService.verifyAndFetchTransactions(authRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/vouchers")
    fun verifyNonceAndGetVouchers(@RequestBody authRequest: PastRecordsRequestDto): ResponseEntity<List<VoucherDto>> {
        val response = pastRecordsService.verifyAndFetchVouchers(authRequest)
        return ResponseEntity.ok(response)
    }

}
