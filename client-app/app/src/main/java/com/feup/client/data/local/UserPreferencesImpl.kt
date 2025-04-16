package com.feup.client.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.feup.client.data.mapper.toSerializable
import com.feup.client.data.mapper.toUser
import com.feup.client.domain.local.UserPreferences
import com.feup.client.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

class UserPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferences {

    private val Context.userDataStore: DataStore<SerializableUser> by dataStore(
        fileName = "user_prefs.json",
        serializer = SerializableUserSerializer
    )

    override suspend fun saveUser(user: User) {
        context.userDataStore.updateData { user.toSerializable() }
    }

    override suspend fun getUser(): User {
        return context.userDataStore.data.first().toUser()
    }

    override suspend fun isRegistered(): Boolean {
        return context.userDataStore.data.first().uuid.isNotBlank()
    }
}