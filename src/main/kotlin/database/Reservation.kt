package database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Reservations : IntIdTable() {
    val locatario = reference("locatario_id", Users)
    val veiculo = reference("veiculo_id", Vehicles)
    val dataInicio = datetime("data_inicio")
    val dataFim = datetime("data_fim")
    val valorTotal = decimal("valor_total", 12, 2).nullable()
    val status = varchar("status", 20).default("PENDING")
}


data class Reservation(
    val id: Int,
    val locatarioId: Int,
    val veiculoId: Int,
    val dataInicio: DateTime,
    val dataFim: DateTime,
    val valorTotal: Double?,
    val status: String
)

fun ResultRow.toReservation() = Reservation(
    id = this[Reservations.id].value,
    locatarioId = this[Reservations.locatario].value,
    veiculoId = this[Reservations.veiculo].value,
    dataInicio = this[Reservations.dataInicio],
    dataFim = this[Reservations.dataFim],
    valorTotal = this[Reservations.valorTotal]?.toDouble(),
    status = this[Reservations.status]
)