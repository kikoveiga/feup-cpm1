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
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class CryptoManagerImpl @Inject constructor() : CryptoManager {

        override val androidKeyStore = "AndroidKeyStore"
        override val rsaAlias = "rsa_key"
        override val ecAlias = "ec_key"

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

    @OptIn(ExperimentalEncodingApi::class)
    override fun encodePublicKeyToBase64(key: PublicKey): String {
        println("Encoded public key: ${Base64.Default.encode(key.encoded)}")
        return Base64.Default.encode(key.encoded)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun encodePrivateKeyToBase64(key: PrivateKey): String {
        println("Encoded private key: ${Base64.Default.encode(key.encoded)}")
        return Base64.Default.encode(key.encoded)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decodePublicKeyFromBase64(encodedKey: String, algorithm: String): PublicKey {
        val bytes = Base64.Default.decode(encodedKey)
        return KeyFactory.getInstance(algorithm).generatePublic(X509EncodedKeySpec(bytes))
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decodePrivateKeyFromBase64(encodedKey: String, algorithm: String): PrivateKey {
        val bytes = Base64.Default.decode(encodedKey)
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
        println("Cleaned public key: $cleaned")
        return decodePublicKeyFromBase64(cleaned, algorithm)
    }
}