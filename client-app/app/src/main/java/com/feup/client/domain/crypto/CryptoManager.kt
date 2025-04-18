package com.feup.client.domain.crypto

import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

interface CryptoManager {
    val androidKeyStore: String
    val rsaAlias: String
    val ecAlias: String

    fun encodeToBase64(data: ByteArray): String
    fun decodeFromBase64(encodedData: String): ByteArray
    fun hashPassword(password: String): ByteArray
    fun generateRSAKeyPair(): KeyPair
    fun generateECKeyPair(): KeyPair
    fun decodePublicKeyFromBase64(encodedKey: String, algorithm: String): PublicKey
    fun decodePrivateKeyFromBase64(encodedKey: String, algorithm: String): PrivateKey
    fun getPrivateKey(alias: String): PrivateKey?
    fun parsePemPublicKey(pem: String, algorithm: String): PublicKey
    fun decryptWithPublicKey(encryptedData: String, publicKey: PublicKey): ByteArray
}