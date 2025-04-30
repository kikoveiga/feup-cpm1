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

    private fun rsaAlias(userNickname: String) = "rsa_key_$userNickname"
    private fun ecAlias(userNickname: String) = "ec_key_$userNickname"

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

    override fun generateRSAKeyPair(userNickname: String): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA, androidKeyStore
        )

        val spec = KeyGenParameterSpec.Builder(
            rsaAlias(userNickname),
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_SIGN
        )
            .setKeySize(512)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .build()

        keyPairGenerator.initialize(spec)
        return keyPairGenerator.generateKeyPair()
    }

    override fun generateECKeyPair(userNickname: String): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC, androidKeyStore
        )

        val spec = KeyGenParameterSpec.Builder(
            ecAlias(userNickname),
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

    override fun getPrivateKey(userNickname: String, isRsa: Boolean): PrivateKey? {
        val keyStore = KeyStore.getInstance(androidKeyStore).apply { load(null) }
        val alias = if (isRsa) rsaAlias(userNickname) else ecAlias(userNickname)
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
        val cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        val encryptedBytes = Base64.decode(encryptedData)
        return cipher.doFinal(encryptedBytes)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun generateSignature(userNickname: String, message: ByteArray): String {
        val privateKey = getPrivateKey(userNickname, isRsa = false)
            ?: throw IllegalStateException("User Private key not found in keystore")

        val signature = java.security.Signature.getInstance("SHA256withECDSA")
        signature.initSign(privateKey)
        signature.update(message)

        val signedBytes = signature.sign()
        return Base64.Default.encode(signedBytes)
    }

    override fun deleteKeys(userNickname: String) {
        val keyStore = KeyStore.getInstance(androidKeyStore).apply { load(null) }

        if (keyStore.containsAlias(rsaAlias(userNickname))) {
            keyStore.deleteEntry(rsaAlias(userNickname))
        }

        if (keyStore.containsAlias(ecAlias(userNickname))) {
            keyStore.deleteEntry(ecAlias(userNickname))
        }
    }
}