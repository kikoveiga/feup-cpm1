package com.feup.client.data.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.feup.client.domain.crypto.CryptoManager
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class CryptoManagerImpl @Inject constructor() : CryptoManager {

    override val androidKeyStore = "AndroidKeyStore"
    override val rsaAlias = "rsa_key"
    override val ecAlias = "ec_key"

    @OptIn(ExperimentalEncodingApi::class)
    override fun encodeToBase64(data: ByteArray): String {
        return Base64.Default.encode(data)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decodeFromBase64(encodedData: String): ByteArray {
        return Base64.Default.decode(encodedData)
    }

    override fun hashPassword(password: String): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), "staticSalt".toByteArray(), 65536, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec).encoded
    }

    override fun generateRSAKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA, androidKeyStore
        )

        val spec = KeyGenParameterSpec.Builder(
            rsaAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_SIGN
        )
            .setKeySize(512)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .build()

        keyPairGenerator.initialize(spec)
        return keyPairGenerator.generateKeyPair()
    }

    override fun generateECKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC, androidKeyStore
        )

        val spec = KeyGenParameterSpec.Builder(
            ecAlias,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            .setDigests(KeyProperties.DIGEST_SHA256)
            .build()

        keyPairGenerator.initialize(spec)
        return keyPairGenerator.generateKeyPair()
    }

    override fun decodePublicKeyFromBase64(encodedKey: String, algorithm: String): PublicKey {
        val bytes = decodeFromBase64(encodedKey)
        return KeyFactory.getInstance(algorithm).generatePublic(X509EncodedKeySpec(bytes))
    }

    override fun decodePrivateKeyFromBase64(encodedKey: String, algorithm: String): PrivateKey {
        val bytes = decodeFromBase64(encodedKey)
        return KeyFactory.getInstance(algorithm).generatePrivate(PKCS8EncodedKeySpec(bytes))
    }

    override fun getPrivateKey(alias: String): PrivateKey? {
        val keyStore = KeyStore.getInstance(androidKeyStore).apply { load(null) }
        return keyStore.getKey(alias, null) as? PrivateKey
    }

    override fun parsePemPublicKey(pem: String, algorithm: String): PublicKey {
        val cleaned = pem
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")
        return decodePublicKeyFromBase64(cleaned, algorithm)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decryptWithPublicKey(encryptedData: String, publicKey: PublicKey): ByteArray {
        println("#Encrypted data: $encryptedData")
        val cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        val encryptedBytes = Base64.decode(encryptedData)
        return cipher.doFinal(encryptedBytes)
    }
}