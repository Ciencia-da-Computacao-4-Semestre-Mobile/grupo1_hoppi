package com.grupo1.hoppi.ui.screens.login.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo1.hoppi.network.ApiClient
import com.grupo1.hoppi.network.auth.ForgotPasswordRequest
import com.grupo1.hoppi.network.auth.VerifyCodeRequest
import com.grupo1.hoppi.network.auth.ResetPasswordRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val email: String = "",
    val code: String = "",
    val newPassword: String = "",

    val loading: Boolean = false,
    val error: String? = null,

    val emailSent: Boolean = false,
    val codeVerified: Boolean = false,
    val passwordReset: Boolean = false
)

class ForgotPasswordViewModel : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state: StateFlow<ForgotPasswordUiState> = _state

    fun setEmail(value: String) {
        _state.update { it.copy(email = value) }
    }

    fun setCode(value: String) {
        _state.update { it.copy(code = value) }
    }

    fun setNewPassword(value: String) {
        _state.update { it.copy(newPassword = value) }
    }

    fun sendRecoveryEmail(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            try {
                ApiClient.auth.forgotPassword(
                    ForgotPasswordRequest(email = state.value.email)
                )

                _state.update {
                    it.copy(
                        loading = false,
                        emailSent = true
                    )
                }

                onSuccess()

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        loading = false,
                        error = e.message ?: "Erro inesperado ao enviar email"
                    )
                }
            }
        }
    }

    fun verifyCode(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            try {
                ApiClient.auth.verifyResetCode(
                    VerifyCodeRequest(
                        email = state.value.email,
                        code = state.value.code
                    )
                )

                _state.update {
                    it.copy(
                        loading = false,
                        codeVerified = true
                    )
                }

                onSuccess()

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        loading = false,
                        error = e.message ?: "Código inválido"
                    )
                }
            }
        }
    }

    fun resetPassword(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            try {
                ApiClient.auth.resetPassword(
                    ResetPasswordRequest(
                        email = state.value.email,
                        code = state.value.code,
                        newPassword = state.value.newPassword
                    )
                )

                _state.update {
                    it.copy(
                        loading = false,
                        passwordReset = true
                    )
                }

                onSuccess()

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        loading = false,
                        error = e.message ?: "Erro ao redefinir a senha"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
