package com.feup.jtp.client_app.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.feup.jtp.client_app.domain.local.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferences {
    private val IS_REGISTERED = booleanPreferencesKey("is_registered")
    private val USER_ID = stringPreferencesKey("user_id")

    override suspend fun setUserRegistered(userId: String) {
        context.dataStore.edit { prefs ->
            prefs[IS_REGISTERED] = true
            prefs[USER_ID] = userId
        }
    }

    override suspend fun isRegistered(): Boolean {
        return context.dataStore.data.first()[IS_REGISTERED] ?: false
    }

    override suspend fun getUserId(): String? {
        return context.dataStore.data.first()[USER_ID]
    }
}