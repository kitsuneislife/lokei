package com.lukas.lokei.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<Account?>(null)
    val currentUser: StateFlow<Account?> = _currentUser

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun register(name: String, email: String, password: String) {
        val success = AccountRepository.register(name, email, password)
        if (success) {
            _currentUser.value = Account(name, email, password)
            _errorMessage.value = null
        } else {
            _errorMessage.value = "Email já cadastrado"
        }
    }

    fun login(email: String, password: String) {
        val acc = AccountRepository.login(email, password)
        if (acc != null) {
            _currentUser.value = acc
            _errorMessage.value = null
        } else {
            _errorMessage.value = "Credenciais inválidas"
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }
}