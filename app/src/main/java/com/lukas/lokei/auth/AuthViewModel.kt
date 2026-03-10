package com.lukas.lokei.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<Account?>(null)
    val currentUser: StateFlow<Account?> = _currentUser

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    private val uploadService = FakeUploadService(viewModelScope)
    val cpfUploadState: StateFlow<UploadState> = uploadService.cpfUploadState()
    val cnhUploadState: StateFlow<UploadState> = uploadService.cnhUploadState()

    fun register(
        name: String,
        email: String,
        password: String,
        cpf: String = "",
        cnh: String = "",
        documentsValidated: Boolean = false,
        bypassUploadGate: Boolean = false
    ) {
        if (!bypassUploadGate && (cpfUploadState.value.isCompleted.not() || cnhUploadState.value.isCompleted.not())) {
            _errorMessage.value = "Finalize o upload de CPF e CNH"
            return
        }

        val success = AccountRepository.register(
            name = name,
            email = email,
            password = password,
            cpf = cpf,
            cnh = cnh,
            documentsValidated = documentsValidated
        )
        if (success) {
            _currentUser.value = Account(
                name = name,
                email = email,
                password = password,
                cpf = cpf,
                cnh = cnh,
                documentsValidated = documentsValidated
            )
            _errorMessage.value = null
            _successMessage.value = "Conta criada com sucesso"
            uploadService.resetAll()
        } else {
            _errorMessage.value = "Email já cadastrado"
        }
    }

    fun login(email: String, password: String) {
        val acc = AccountRepository.login(email, password)
        if (acc != null) {
            _currentUser.value = acc
            _errorMessage.value = null
            _successMessage.value = "Login realizado"
        } else {
            _errorMessage.value = "Credenciais inválidas"
        }
    }

    fun logout() {
        _currentUser.value = null
        _successMessage.value = "Logout realizado"
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccess() {
        _successMessage.value = null
    }

    fun startCpfUpload() {
        uploadService.startCpfUpload()
        _successMessage.value = "Upload de CPF iniciado"
    }

    fun startCnhUpload() {
        uploadService.startCnhUpload()
        _successMessage.value = "Upload de CNH iniciado"
    }

    fun updateCurrentProfile(
        name: String,
        cpf: String,
        cnh: String,
        bankData: String,
        paymentMethod: String
    ) {
        val current = _currentUser.value
        if (current == null) {
            _errorMessage.value = "Usuário não autenticado"
            return
        }

        val updated = current.copy(
            name = name,
            cpf = cpf,
            cnh = cnh,
            bankData = bankData,
            paymentMethod = paymentMethod
        )
        _currentUser.value = AccountRepository.updateProfile(updated)
        _successMessage.value = "Perfil atualizado"
    }
}