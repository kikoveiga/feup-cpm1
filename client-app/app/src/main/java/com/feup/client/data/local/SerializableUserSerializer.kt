package com.feup.client.data.local

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SerializableUserSerializer : Serializer<SerializableUser> {

    override val defaultValue: SerializableUser
        get() = SerializableUser("", "", "", "", "", "", "", "", "")

    override suspend fun readFrom(input: InputStream): SerializableUser {
        return try {
            Json.decodeFromString(
                SerializableUser.serializer(),
                 input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SerializableUser, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(SerializableUser.serializer(), t).encodeToByteArray())
        }
    }
}