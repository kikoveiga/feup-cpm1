package com.feup.jtp.client_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.jtp.client_app.domain.model.PaymentCard
import com.feup.jtp.client_app.domain.model.User
import com.feup.jtp.client_app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onNicknameChanged(nickname: String) {
        _uiState.update { it.copy(nickname = nickname) }
    }

    fun onPaymentCardNumberChanged(paymentCardNumber: String) {
        _uiState.update { it.copy(paymentCardNumber = paymentCardNumber) }
    }

    fun onExpirationDateChanged(expirationDate: String) {
        _uiState.update { it.copy(expirationDate = expirationDate) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun registerUser() {
        val user = User(
            uuid = "",
            name = _uiState.value.name,
            nickname = _uiState.value.nickname,
            paymentCard = PaymentCard(
                id = "",
                type = "",
                number = _uiState.value.paymentCardNumber,
                expirationDate = _uiState.value.expirationDate
            ),
            publicRSAKey = "mocked_rsa_key",
            publicECKey = "mocked_ec_key"
        )

        viewModelScope.launch {

            _uiState.update { it.copy(showValidationErrors = true) }
            val result = userRepository.registerUser(user)

            if (result.isSuccess) {
                val registeredUser = result.getOrNull()
                _uiState.update { it.copy(user = registeredUser) }
            } else {
                val error = result.exceptionOrNull()
                _uiState.update { it.copy(error = error?.message) }
            }
        }
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.value.user?.uuid?.let { it ->
                val transactions = userRepository.getTransactions(it)
                _uiState.update { it.copy(transactions = transactions) }
            }
        }
    }
}