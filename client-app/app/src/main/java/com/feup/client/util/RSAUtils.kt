package com.feup.client.util

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


}
