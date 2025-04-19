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

        // val supermarketRsaPublicKey = userDataStore.getSupermarketRsaPublicKey()
        val supermarketRsaPublicKey = cryptoManager.parsePemPublicKey("""-----BEGIN PUBLIC KEY-----
MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANtswixMkVTMuDjXbJygsi/A3AWUbk0v
zZ/J6aHGo/zNnXY/Tc1tBA49oYwmO/h61El5U/n1boEZENR6SeNtKssCAwEAAQ==
-----END PUBLIC KEY-----

""", "RSA")

        val eencryptedData = "rVjdUqQdvdyW5mgU4q/6ThUGHj2g0WDYpKmPLUIsOY3aA8iYbGXU2f716C2bLhaQ5b7XKsuIpUq0KSs9Ev5Kww=="
        val decryptedData = cryptoManager.decryptWithPublicKey(eencryptedData, supermarketRsaPublicKey)
        val buffer = ByteBuffer.wrap(decryptedData)

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