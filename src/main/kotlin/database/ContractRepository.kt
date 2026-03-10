package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object ContractRepository {
    fun add(
        reservaId: Int,
        termos: String,
        dataGeracao: DateTime
    ): Contract {
        val id = transaction {
            Contracts.insertAndGetId {
                it[Contracts.reserva] = reservaId
                it[Contracts.termos] = termos
                it[Contracts.dataGeracao] = dataGeracao
            }.value
        }
        return Contract(id, reservaId, termos, dataGeracao)
    }

    fun all(): List<Contract> = transaction {
        Contracts.selectAll().map { it.toContract() }
    }
}