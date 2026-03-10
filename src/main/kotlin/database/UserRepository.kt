package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    fun add(name: String): User {
        var userId = 0
        transaction {
            userId = Users.insert {
                it[Users.name] = name
            } get Users.id
        }.value
        return User(userId, name)
    }

    fun all(): List<User> = transaction {
        Users.selectAll().map { it.toUser() }
    }
}
