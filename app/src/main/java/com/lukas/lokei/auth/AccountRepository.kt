package com.lukas.lokei.auth

data class Account(
    val name: String,
    val email: String,
    val password: String,
    val cpf: String = "",
    val cnh: String = "",
    val bankData: String = "",
    val paymentMethod: String = "",
    val documentsValidated: Boolean = false
)

object AccountRepository {
    private val accounts = mutableMapOf<String, Account>()

    /**
     * Registers a new account. Returns true if successful, false if email already exists.
     */
    fun register(
        name: String,
        email: String,
        password: String,
        cpf: String = "",
        cnh: String = "",
        documentsValidated: Boolean = false
    ): Boolean {
        if (accounts.containsKey(email)) return false
        accounts[email] = Account(
            name = name,
            email = email,
            password = password,
            cpf = cpf,
            cnh = cnh,
            documentsValidated = documentsValidated
        )
        return true
    }

    /**
     * Attempts login; returns the Account on success or null on failure.
     */
    fun login(email: String, password: String): Account? {
        val acc = accounts[email] ?: return null
        return if (acc.password == password) acc else null
    }

    fun updateProfile(updated: Account): Account {
        val stored = accounts[updated.email] ?: updated
        val merged = stored.copy(
            name = updated.name,
            cpf = updated.cpf,
            cnh = updated.cnh,
            bankData = updated.bankData,
            paymentMethod = updated.paymentMethod,
            documentsValidated = updated.documentsValidated
        )
        accounts[merged.email] = merged
        return merged
    }

    fun clear() {
        accounts.clear()
    }
}