package database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object Reviews : IntIdTable() {
    val reserva = reference("reserva_id", Reservations)
    val usuarioAvaliador = reference("usuario_avaliador_id", Users)
    val pontuacao = integer("pontuacao")
    val comentario = varchar("comentario", 1000).nullable()
    val data = datetime("data")
}

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

data class Review(
    val id: Int,
    val reservaId: Int,
    val usuarioAvaliadorId: Int,
    val pontuacao: Int,
    val comentario: String?,
    val data: DateTime
)

fun ResultRow.toReview() = Review(
    id = this[Reviews.id].value,
    reservaId = this[Reviews.reserva].value,
    usuarioAvaliadorId = this[Reviews.usuarioAvaliador].value,
    pontuacao = this[Reviews.pontuacao],
    comentario = this[Reviews.comentario],
    data = this[Reviews.data]
)