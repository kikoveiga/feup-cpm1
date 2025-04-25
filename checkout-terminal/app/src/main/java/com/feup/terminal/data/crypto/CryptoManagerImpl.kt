package com.feup.terminal.data.crypto

import com.feup.terminal.domain.crypto.CryptoManager
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class CryptoManagerImpl @Inject constructor() : CryptoManager {

    @OptIn(ExperimentalEncodingApi::class)
    override fun decodeFromBase64(encodedData: String): ByteArray {
        return Base64.Default.decode(encodedData)
    }
}