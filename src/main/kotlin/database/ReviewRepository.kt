package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object ReviewRepository {
    fun add(
        reservaId: Int,
        usuarioAvaliadorId: Int,
        pontuacao: Int,
        comentario: String?,
        data: DateTime
    ): Review {
        val id = transaction {
            Reviews.insertAndGetId {
                it[Reviews.reserva] = reservaId
                it[Reviews.usuarioAvaliador] = usuarioAvaliadorId
                it[Reviews.pontuacao] = pontuacao
                it[Reviews.comentario] = comentario
                it[Reviews.data] = data
            }.value
        }
        return Review(id, reservaId, usuarioAvaliadorId, pontuacao, comentario, data)
    }

    fun all(): List<Review> = transaction {
        Reviews.selectAll().map { it.toReview() }
    }
}