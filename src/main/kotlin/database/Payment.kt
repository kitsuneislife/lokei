package database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Payments : IntIdTable() {
    val reserva = reference("reserva_id", Reservations)
    val valor = decimal("valor", 12, 2)
    val metodo = varchar("metodo", 50)
    val dataPagamento = datetime("data_pagamento")
    val status = varchar("status", 20).default("CREATED")
}


data class Payment(
    val id: Int,
    val reservaId: Int,
    val valor: Double,
    val metodo: String,
    val dataPagamento: DateTime,
    val status: String
)

fun ResultRow.toPayment() = Payment(
    id = this[Payments.id].value,
    reservaId = this[Payments.reserva].value,
    valor = this[Payments.valor].toDouble(),
    metodo = this[Payments.metodo],
    dataPagamento = this[Payments.dataPagamento],
    status = this[Payments.status]
)