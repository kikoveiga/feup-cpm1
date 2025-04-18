package com.acme.supermarket.server.service

import org.springframework.stereotype.Component

@Component
class NonceStore {
    private val nonceMap = mutableMapOf<String, String>()

    fun storeNonce(userUuid: String, nonce: String) {
        nonceMap[userUuid] = nonce
    }

    fun getNonce(userUuid: String): String? = nonceMap[userUuid]
}
