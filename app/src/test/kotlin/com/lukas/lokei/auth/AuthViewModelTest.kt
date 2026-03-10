package com.lukas.lokei.auth

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        AccountRepository.clear()
        viewModel = AuthViewModel()
    }

    @Test
    fun successfulRegisterUpdatesUser() = runBlocking {
        viewModel.register("Bob", "bob@example.com", "pwd")
        val user = viewModel.currentUser.first()
        assertNotNull(user)
        assertEquals("Bob", user?.name)
    }

    @Test
    fun loginWithInvalidCredsSetsError() = runBlocking {
        viewModel.login("x@x.com", "123")
        val err = viewModel.errorMessage.first()
        assertEquals("Credenciais inválidas", err)
    }
}