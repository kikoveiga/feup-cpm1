package com.feup.client.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.feup.client.data.mapper.toSerializable
import com.feup.client.data.mapper.toUser
import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.security.PublicKey
import javax.inject.Inject

class UserDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoManager: CryptoManager
) : UserDataStore {

    private val Context.userDataStore: DataStore<SerializableUser> by dataStore(
        fileName = "user_prefs.json",
        serializer = SerializableUserSerializer
    )

    override suspend fun saveUser(user: User) {
        context.userDataStore.updateData { user.toSerializable(cryptoManager) }
    }

    override suspend fun getUser(): User {
        return context.userDataStore.data.first().toUser(cryptoManager)
    }

    override suspend fun getSupermarketRsaPublicKey(): PublicKey {
        context.userDataStore.data.first().supermarketRsaPublicKey.let {
            return cryptoManager.decodePublicKeyFromBase64(it, "RSA")
        }
    }

    override suspend fun isRegistered(): Boolean {
        return context.userDataStore.data.first().uuid.isNotBlank()
    }
}