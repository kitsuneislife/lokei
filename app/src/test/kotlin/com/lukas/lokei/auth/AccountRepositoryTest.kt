package com.lukas.lokei.auth

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AccountRepositoryTest {
    @Before
    fun setup() {
        AccountRepository.clear()
    }

    @Test
    fun registerAndLogin_success() {
        val result = AccountRepository.register("Alice", "alice@example.com", "pwd123")
        assertTrue(result)
        val acc = AccountRepository.login("alice@example.com", "pwd123")
        assertNotNull(acc)
        assertEquals("Alice", acc?.name)
    }

    @Test
    fun register_duplicateEmailFails() {
        AccountRepository.register("A", "a@example.com", "1")
        val second = AccountRepository.register("B", "a@example.com", "2")
        assertFalse(second)
    }

    @Test
    fun login_wrongPasswordFails() {
        AccountRepository.register("A", "a@example.com", "1")
        val acc = AccountRepository.login("a@example.com", "wrong")
        assertNull(acc)
    }
}