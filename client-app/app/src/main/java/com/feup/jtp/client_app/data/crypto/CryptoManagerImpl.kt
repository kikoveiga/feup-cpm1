package com.feup.jtp.client_app.data.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.feup.jtp.client_app.domain.crypto.CryptoManager
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.spec.ECGenParameterSpec
import javax.inject.Inject

class CryptoManagerImpl @Inject constructor() : CryptoManager {

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val RSA_ALIAS = "rsa_key"
        private const val EC_ALIAS = "ec_key"
    }

    override fun generateECKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEYSTORE
        )

        val spec = KeyGenParameterSpec.Builder(
            RSA_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_SIGN
        )
            .setKeySize(512)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .build()

        keyPairGenerator.initialize(spec)
        return keyPairGenerator.generateKeyPair()
    }

    override fun generateRSAKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC, ANDROID_KEYSTORE
        )

        val spec = KeyGenParameterSpec.Builder(
            EC_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            .setDigests(KeyProperties.DIGEST_SHA256)
            .build()

        keyPairGenerator.initialize(spec)
        return keyPairGenerator.generateKeyPair()
    }

    override fun storePrivateKey(alias: String, key: PrivateKey) {
        TODO("Not yet implemented")
    }

    override fun getPrivateKey(alias: String): PrivateKey? {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return keyStore.getKey(alias, null) as? PrivateKey
    }
}