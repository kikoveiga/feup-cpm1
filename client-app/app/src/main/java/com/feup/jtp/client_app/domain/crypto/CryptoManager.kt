package com.feup.jtp.client_app.domain.crypto

import java.security.KeyPair
import java.security.PrivateKey

interface CryptoManager {
    fun generateRSAKeyPair(): KeyPair
    fun generateECKeyPair(): KeyPair
    fun storePrivateKey(alias: String, key: PrivateKey)
    fun getPrivateKey(alias: String): PrivateKey?
}