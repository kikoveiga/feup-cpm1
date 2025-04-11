package com.feup.jtp.client_app.util

import java.security.PublicKey
import javax.crypto.Cipher
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object RSAUtils {
    fun decrypt(data: ByteArray, publicKey: PublicKey): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        return cipher.doFinal(data)
    }

    // Temporary method to generate a mock public key for testing
    fun generateMockPublicKey(): PublicKey {
        // Generate RSA key pair
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048) // 2048-bit key size (strong enough for testing)
        val keyPair: KeyPair = keyPairGenerator.generateKeyPair()

        // Return the public key from the key pair
        return keyPair.public
    }

    // Convert the public key to a Base64-encoded string
    fun getMockPublicKeyBase64(): String {
        val publicKey = generateMockPublicKey()
        val encodedKey = publicKey.encoded
        return Base64.getEncoder().encodeToString(encodedKey)
    }

    // Load public key from a Base64-encoded string (mock key)
    fun loadPublicKey(): PublicKey {
        val publicKeyBase64 = getMockPublicKeyBase64()

        // Decode the Base64 string into bytes
        val encodedKey = Base64.getDecoder().decode(publicKeyBase64)

        // Generate the public key from the encoded bytes
        val keySpec = X509EncodedKeySpec(encodedKey)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }
}
