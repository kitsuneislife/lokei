package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    fun add(name: String): User {
        val userId = transaction {
            Users.insertAndGetId {
                it[Users.name] = name
            }.value
        }
        return User(
            id = userId,
            name = name,
            email = null,
            phone = null,
            cpf = null,
            status = "ACTIVE",
            cnh = null,
            bankData = null,
            permissionLevel = null
        )
    }

    fun all(): List<User> = transaction {
        Users.selectAll().map { it.toUser() }
    }
}
