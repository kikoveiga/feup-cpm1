package com.feup.client.domain.usecases

import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.model.Product
import java.nio.ByteBuffer
import java.util.UUID
import javax.inject.Inject

class ScanProductUseCase @Inject constructor(
    private val userDataStore: UserDataStore,
    private val cryptoManager: CryptoManager
) {
    suspend fun invoke(encryptedData: String): Product {

        val supermarketRsaPublicKey = userDataStore.getSupermarketRsaPublicKey()

        println(cryptoManager.decodeFromBase64(encryptedData))
        val buffer = ByteBuffer.wrap(cryptoManager.decodeFromBase64(encryptedData))

        val uuidBytes = ByteArray(16) // UUID is 16 bytes
        buffer.get(uuidBytes)
        val uuid = UUID.nameUUIDFromBytes(uuidBytes).toString()

        val price = buffer.float

        val nameBytes = ByteArray(buffer.remaining())
        buffer.get(nameBytes)
        val name = String(nameBytes, Charsets.UTF_8)

        println("#UUID: $uuid")
        println("#Name: $name")
        println("#Price: $price")

        return Product(
            uuid = uuid,
            name = name,
            price = price.toDouble()
        )
    }
}