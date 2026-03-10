package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object ReservationRepository {
    fun add(
        locatarioId: Int,
        veiculoId: Int,
        dataInicio: DateTime,
        dataFim: DateTime,
        valorTotal: Double?,
        status: String = "PENDING"
    ): Reservation {
        var id = 0
        transaction {
            id = Reservations.insert {
                it[Reservations.locatario] = locatarioId
                it[Reservations.veiculo] = veiculoId
                it[Reservations.dataInicio] = dataInicio
                it[Reservations.dataFim] = dataFim
                if (valorTotal != null) it[Reservations.valorTotal] = valorTotal.toBigDecimal()
                it[Reservations.status] = status
            } get Reservations.id
        }.value
        return Reservation(id, locatarioId, veiculoId, dataInicio, dataFim, valorTotal, status)
    }

    fun all(): List<Reservation> = transaction {
        Reservations.selectAll().map { it.toReservation() }
    }
}