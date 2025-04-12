package com.feup.jtp.client_app.domain.interactor

import com.feup.jtp.client_app.domain.model.Product
import com.feup.jtp.client_app.util.RSAUtils
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.security.PublicKey
import java.util.UUID

class ScanProductUseCase(
    private val rsaPublicKey: PublicKey
) {
    fun execute(encryptedData: ByteArray): Product? {
        try {
            val decryptedBytes = RSAUtils.decrypt(encryptedData, rsaPublicKey)

            val buffer = ByteBuffer.wrap(decryptedBytes)

            val uuidBytes = ByteArray(16)
            buffer.get(uuidBytes)
            val uuid = UUID.nameUUIDFromBytes((uuidBytes))

            val euros = buffer.get().toInt()
            val cents = buffer.get().toInt()
            val price = euros + cents / 100.0

            val nameBytes = ByteArray(buffer.remaining())
            buffer.get(nameBytes)
            val name = String(nameBytes, Charsets.UTF_8)

            return Product(uuid.toString(), name, price)
        } catch (e:Exception) {
            e.printStackTrace()
            return null
        }
    }
}