package com.lukas.lokei.auth

data class Account(val name: String, val email: String, val password: String)

object AccountRepository {
    private val accounts = mutableMapOf<String, Account>()

    /**
     * Registers a new account. Returns true if successful, false if email already exists.
     */
    fun register(name: String, email: String, password: String): Boolean {
        if (accounts.containsKey(email)) return false
        accounts[email] = Account(name, email, password)
        return true
    }

    /**
     * Attempts login; returns the Account on success or null on failure.
     */
    fun login(email: String, password: String): Account? {
        val acc = accounts[email] ?: return null
        return if (acc.password == password) acc else null
    }

    fun clear() {
        accounts.clear()
    }
}