package database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object Users : IntIdTable() {
    val name = varchar("name", 50)
    val email = varchar("email", 100).nullable()
    val phone = varchar("phone", 20).nullable()
    val cpf = varchar("cpf", 14).nullable()
    val status = varchar("status", 20).default("ACTIVE")
    // profile-specific attributes
    val cnh = varchar("cnh", 20).nullable()              // locatário
    val bankData = varchar("bank_data", 200).nullable()   // proprietário
    val permissionLevel = varchar("permission_level", 20).nullable() // administrador
}

data class User(
    val id: Int,
    val name: String,
    val email: String?,
    val phone: String?,
    val cpf: String?,
    val status: String,
    val cnh: String?,
    val bankData: String?,
    val permissionLevel: String?
)

fun ResultRow.toUser() = User(
    id = this[Users.id].value,
    name = this[Users.name],
    email = this[Users.email],
    phone = this[Users.phone],
    cpf = this[Users.cpf],
    status = this[Users.status],
    cnh = this[Users.cnh],
    bankData = this[Users.bankData],
    permissionLevel = this[Users.permissionLevel]
)
