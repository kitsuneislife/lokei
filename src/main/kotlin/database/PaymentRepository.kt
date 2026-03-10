package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object PaymentRepository {
    fun add(
        reservaId: Int,
        valor: Double,
        metodo: String,
        dataPagamento: DateTime,
        status: String = "CREATED"
    ): Payment {
        val id = transaction {
            Payments.insertAndGetId {
                it[Payments.reserva] = reservaId
                it[Payments.valor] = valor.toBigDecimal()
                it[Payments.metodo] = metodo
                it[Payments.dataPagamento] = dataPagamento
                it[Payments.status] = status
            }.value
        }
        return Payment(id, reservaId, valor, metodo, dataPagamento, status)
    }

    fun all(): List<Payment> = transaction {
        Payments.selectAll().map { it.toPayment() }
    }
}