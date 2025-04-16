package com.feup.client.domain.interactor

import com.feup.client.domain.model.Product
import com.feup.client.util.RSAUtils
import java.nio.ByteBuffer
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