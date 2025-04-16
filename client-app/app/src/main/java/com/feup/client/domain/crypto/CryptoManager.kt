package com.feup.client.domain.crypto

import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

interface CryptoManager {
    val androidKeyStore: String
    val rsaAlias: String
    val ecAlias: String

    fun generateRSAKeyPair(): KeyPair
    fun generateECKeyPair(): KeyPair
    fun encodePublicKeyToBase64(key: PublicKey): String
    fun encodePrivateKeyToBase64(key: PrivateKey): String
    fun decodePublicKeyFromBase64(encodedKey: String, algorithm: String): PublicKey
    fun decodePrivateKeyFromBase64(encodedKey: String, algorithm: String): PrivateKey
    fun getPrivateKey(alias: String): PrivateKey?
    fun parsePemPublicKey(pem: String, algorithm: String): PublicKey
}