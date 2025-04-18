package com.acme.supermarket.server.controller

import com.acme.supermarket.server.dto.AuthResponseDto
import com.acme.supermarket.server.dto.AuthVerificationRequestDto
import com.acme.supermarket.server.dto.NonceResponseDto
import com.acme.supermarket.server.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @GetMapping("/nonce")
    fun getNonce(@RequestParam uuid: String): ResponseEntity<NonceResponseDto> {
        val nonce = authService.generateAndStoreNonce(uuid)
        return ResponseEntity.ok(NonceResponseDto(nonce))
    }

    @PostMapping("/verify")
    fun verifyNonceAndGetData(@RequestBody authRequest: AuthVerificationRequestDto): ResponseEntity<AuthResponseDto> {
        val response = authService.verifyAndFetchUserData(authRequest)
        return ResponseEntity.ok(response)
    }


}
