package com.feup.client.domain.interactor

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
        val decryptedData = cryptoManager.decryptWithPublicKey(encryptedData, supermarketRsaPublicKey)
        val buffer = ByteBuffer.wrap(decryptedData)

        val uuidBytes = ByteArray(16) // UUID is 16 bytes
        buffer.get(uuidBytes)
        val uuid = UUID.nameUUIDFromBytes(uuidBytes).toString()

        val price = buffer.float

        val nameBytes = ByteArray(buffer.remaining())
        buffer.get(nameBytes)
        val name = String(nameBytes, Charsets.UTF_8)

        return Product(
            uuid = uuid,
            name = name,
            price = price.toDouble()
        )
    }
}