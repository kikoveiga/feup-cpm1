package com.feup.client.data.local.datastore

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SerializableUsersStateSerializer : Serializer<SerializableUsersState> {

    override val defaultValue: SerializableUsersState
        get() = SerializableUsersState(emptyList(), null)

    override suspend fun readFrom(input: InputStream): SerializableUsersState {
        return try {
            Json.decodeFromString(
                SerializableUsersState.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SerializableUsersState, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(SerializableUsersState.serializer(), t).encodeToByteArray())
        }
    }
}