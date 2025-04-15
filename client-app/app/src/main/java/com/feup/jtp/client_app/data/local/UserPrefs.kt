package com.feup.jtp.client_app.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPrefs {
    private val IS_REGISTERED = booleanPreferencesKey("is_registered")
    private val USER_ID = stringPreferencesKey("user_id")

    suspend fun setRegistered(context: Context, userId: String) {
        context.dataStore.edit { prefs ->
            prefs[IS_REGISTERED] = true
            prefs[USER_ID] = userId
        }
    }

    suspend fun isRegistered(context: Context): Boolean {
        return context.dataStore.data.first()[IS_REGISTERED] ?: false
    }

    suspend fun getUserId(context: Context): String? {
        return context.dataStore.data.first()[USER_ID]
    }
}