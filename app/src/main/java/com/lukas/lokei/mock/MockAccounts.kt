package com.lukas.lokei.mock

import com.lukas.lokei.auth.Account

object MockAccounts {
    val proprietor = Account(
        name = "Proprietário Lokei",
        email = "proprietario",
        password = "proprietario",
        cpf = "111.111.111-11",
        cnh = "99999999999",
        bankData = "Banco Lokei • Ag 0001 • Cc 12345-6",
        paymentMethod = "Recebimento via PIX",
        documentsValidated = true,
        role = "PROPRIETARIO"
    )

    val renter = Account(
        name = "Locatário Lokei",
        email = "locatario",
        password = "locatario",
        cpf = "222.222.222-22",
        cnh = "88888888888",
        documentsValidated = true,
        role = "LOCATARIO"
    )

    val admin = Account(
        name = "Administrador Lokei",
        email = "admin",
        password = "admin",
        cpf = "333.333.333-33",
        documentsValidated = true,
        role = "ADMIN"
    )

    val defaults = listOf(proprietor, renter, admin)
}
