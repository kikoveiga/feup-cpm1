package com.acme.supermarket.server.service

import org.springframework.stereotype.Service
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.security.Signature

@Service
class CryptoService {

    fun verifyEcSignature(publicKeyBase64: String, message: String, signatureBase64: String): Boolean {
        try {
            val keyBytes = Base64.getDecoder().decode(publicKeyBase64)
            val keySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance("EC")
            val publicKey = keyFactory.generatePublic(keySpec)

            val signature = Signature.getInstance("SHA256withECDSA")
            signature.initVerify(publicKey)
            signature.update(message.toByteArray())

            val signatureBytes = Base64.getDecoder().decode(signatureBase64)
            return signature.verify(signatureBytes)

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
