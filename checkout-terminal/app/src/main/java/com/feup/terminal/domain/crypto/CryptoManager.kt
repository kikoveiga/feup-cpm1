package com.feup.terminal.domain.crypto

interface CryptoManager {
    fun decodeFromBase64(encodedData: String): ByteArray
}