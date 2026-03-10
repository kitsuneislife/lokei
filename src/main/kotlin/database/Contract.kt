package database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Contracts : IntIdTable() {
    val reserva = reference("reserva_id", Reservations)
    val termos = varchar("termos", 2000)
    val dataGeracao = datetime("data_geracao")
}


data class Contract(
    val id: Int,
    val reservaId: Int,
    val termos: String,
    val dataGeracao: DateTime
)

fun ResultRow.toContract() = Contract(
    id = this[Contracts.id].value,
    reservaId = this[Contracts.reserva].value,
    termos = this[Contracts.termos],
    dataGeracao = this[Contracts.dataGeracao]
)