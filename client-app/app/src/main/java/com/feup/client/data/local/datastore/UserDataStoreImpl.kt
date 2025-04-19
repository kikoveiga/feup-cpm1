package com.feup.client.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.feup.client.data.mapper.toSerializable
import com.feup.client.data.mapper.toUser
import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.PublicKey
import javax.inject.Inject

class UserDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoManager: CryptoManager
) : UserDataStore {

    private val Context.userDataStore: DataStore<SerializableUsersState> by dataStore(
        fileName = "users_state.json",
        serializer = SerializableUsersStateSerializer
    )

    override val isLoggedInFlow: Flow<Boolean>
        get() = context.userDataStore.data.map { it.loggedInUserNickname != null }

    override suspend fun doesUserExist(nickname: String): Boolean {
        return context.userDataStore.data.first().users.any { it.nickname == nickname }
    }

    override suspend fun saveUser(user: User) {
        context.userDataStore.updateData { current ->
            val existingUser = current.users.find { it.nickname == user.nickname }
            if (existingUser != null) {
                current.copy(
                    users = current.users - existingUser + user.toSerializable(
                        cryptoManager
                    )
                )
            } else {
                current.copy(
                    users = current.users + user.toSerializable(cryptoManager),
                    loggedInUserNickname = user.nickname
                )
            }
        }
    }

    override suspend fun login(nickname: String, password: String) {
        context.userDataStore.updateData { current ->
            val user = current.users.find { it.nickname == nickname }
                ?: throw IllegalArgumentException("User not found")

            val passwordHash = cryptoManager.hashPassword(password)
            val encodedHash = cryptoManager.encodeToBase64(passwordHash)
            if (!encodedHash.contentEquals(user.passwordHash)) {
                throw IllegalArgumentException("Invalid password")
            }

            current.copy(loggedInUserNickname = user.nickname)
        }
    }

    override suspend fun logoutUser() {
        context.userDataStore.updateData { current ->
            current.copy(loggedInUserNickname = null)
        }
    }

    override suspend fun getLoggedInUser(): User {
        val state = context.userDataStore.data.first()
        val user = state.users.find { it.nickname == state.loggedInUserNickname }
            ?: throw IllegalArgumentException("No user is logged in")
        return user.toUser(cryptoManager)
    }

    override suspend fun getSupermarketRsaPublicKey(): PublicKey {
        context.userDataStore.data.first().let { state ->
            val user = state.users.find { it.nickname == state.loggedInUserNickname }
                ?: throw IllegalArgumentException("No user is logged in")
            return cryptoManager.parsePemPublicKey(user.supermarketRsaPublicKey, "RSA")
        }
    }
}
