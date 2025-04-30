package com.feup.client.domain.crypto

import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

interface CryptoManager {
    val androidKeyStore: String

    fun encodeToBase64(data: ByteArray): String
    fun decodeFromBase64(encodedData: String): ByteArray
    fun hashPassword(password: String): ByteArray
    fun generateRSAKeyPair(userNickname: String): KeyPair
    fun generateECKeyPair(userNickname: String): KeyPair
    fun decodePublicKeyFromBase64(encodedKey: String, algorithm: String): PublicKey
    fun decodePrivateKeyFromBase64(encodedKey: String, algorithm: String): PrivateKey
    fun getPrivateKey(userNickname: String, isRsa: Boolean): PrivateKey?
    fun parsePemPublicKey(pem: String, algorithm: String): PublicKey
    fun decryptWithPublicKey(encryptedData: String, publicKey: PublicKey): ByteArray
    fun generateSignature(userNickname: String, message: ByteArray): String
    fun deleteKeys(userNickname: String)
}