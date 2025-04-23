package com.feup.client.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.local.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val cryptoManager: CryptoManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState?>(null)
    val uiState: StateFlow<ProfileUiState?> = _uiState

    init {
        viewModelScope.launch {
            val user = userDataStore.getLoggedInUser()
            _uiState.value = ProfileUiState(
                user = user,
                error = null
            )
        }
    }

    fun onNewPasswordChanged(password: String) {
        _uiState.update {
            it?.copy(newPassword = password)
        }
    }

    fun clearSnackbarMessage() {
        _uiState.update { it?.copy(snackbarMessage = null) }
    }

    fun updatePassword() {

        val state = _uiState.value ?: return

        val currentUser = state.user
        val newPasswordHash = cryptoManager.hashPassword(state.newPassword)
        viewModelScope.launch {
            if (newPasswordHash.contentEquals(state.user.passwordHash)) {
                _uiState.update { it?.copy(snackbarMessage = "New password cannot be the same as the old one") }
                return@launch
            }

            try {
                userDataStore.saveUser(currentUser.copy(passwordHash = newPasswordHash))
                _uiState.update { it?.copy(newPassword = "", snackbarMessage = "Password updated successfully!") }
            } catch (e:Exception) {
                _uiState.update { it?.copy(snackbarMessage = e.message ?: "Failed to update password") }
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {

            try {
                userDataStore.logoutUser()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    error = e.message ?: "Failed to logout user"
                )
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                userDataStore.deleteUser()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    error = e.message ?: "Failed to delete account"
                )
            }
        }
    }
}
