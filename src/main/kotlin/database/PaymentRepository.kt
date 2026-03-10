package database

import org.jetbrains.exposed.sql.insert
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
        var id = 0
        transaction {
            id = Payments.insert {
                it[Payments.reserva] = reservaId
                it[Payments.valor] = valor.toBigDecimal()
                it[Payments.metodo] = metodo
                it[Payments.dataPagamento] = dataPagamento
                it[Payments.status] = status
            } get Payments.id
        }.value
        return Payment(id, reservaId, valor, metodo, dataPagamento, status)
    }

    fun all(): List<Payment> = transaction {
        Payments.selectAll().map { it.toPayment() }
    }
}