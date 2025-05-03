package com.feup.client.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.client.domain.model.PaymentCard
import com.feup.client.domain.model.PaymentCardType
import com.feup.client.domain.usecases.AuthUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUserUseCase: AuthUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    val isLoginFormValid: Boolean
        get() = _uiState.value.run { nickname.length >= 4 && password.length >= 4}

    val isRegisterFormValid: Boolean
        get() = isLoginFormValid && _uiState.value.run {
            name.length >= 2 && paymentCardNumber.length >= 12
        }

    fun onNicknameChanged(nickname: String) {
        _uiState.update { it.copy(nickname = nickname) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onPaymentCardTypeChanged(paymentCardType: PaymentCardType) {
        _uiState.update { it.copy(paymentCardType = paymentCardType) }
    }

    fun onPaymentCardNumberChanged(paymentCardNumber: String) {
        _uiState.update { it.copy(paymentCardNumber = paymentCardNumber) }
    }

    fun onPaymentCardExpirationDateChanged(paymentCardExpirationDate: String) {
        _uiState.update { it.copy(paymentCardExpirationDate = paymentCardExpirationDate) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun registerUser() {
        viewModelScope.launch {

            val result = _uiState.value.let {
                authUserUseCase.register(
                    name = it.name,
                    nickname = it.nickname,
                    password = it.password,
                    paymentCard = PaymentCard(it.paymentCardType, it.paymentCardNumber, it.paymentCardExpirationDate)
                )
            }

            if (result.isSuccess) {
                _uiState.update { AuthUiState() }
            } else {
                val error = result.exceptionOrNull()
                _uiState.update { it.copy(error = error?.message) }
            }
        }
    }

    fun loginUser() {
        viewModelScope.launch {

            val result = _uiState.value.let {
                authUserUseCase.login(
                    nickname = it.nickname,
                    password = it.password,
                )
            }

            if (result.isSuccess) {
                _uiState.update { AuthUiState() }
            } else {
                val error = result.exceptionOrNull()
                _uiState.update { it.copy(error = error?.message) }
            }
        }
    }
}
