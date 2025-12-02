package com.grupo1.hoppi.ui.screens.login.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo1.hoppi.network.ApiClient
import com.grupo1.hoppi.network.auth.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        _uiState.value = LoginUiState.Loading
        println("DEBUG: Iniciando login com $email / $password")

        viewModelScope.launch {
            try {
                val response = ApiClient.auth.login(LoginRequest(email, password))
                println("DEBUG: Response recebida: $response")

                if (response.access_token != null && response.user != null) {
                    _uiState.value = LoginUiState.Success(
                        token = response.access_token,
                        username = response.user.username,
                        id = response.user.id
                    )
                } else {
                    _uiState.value = LoginUiState.Error("Token ou usuário não recebido")
                }

            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

}

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(
        val token: String,
        val username: String,
        val id: String
    ) : LoginUiState
    data class Error(val message: String) : LoginUiState
}
