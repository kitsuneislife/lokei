package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object ContractRepository {
    fun add(
        reservaId: Int,
        termos: String,
        dataGeracao: DateTime
    ): Contract {
        var id = 0
        transaction {
            id = Contracts.insert {
                it[Contracts.reserva] = reservaId
                it[Contracts.termos] = termos
                it[Contracts.dataGeracao] = dataGeracao
            } get Contracts.id
        }.value
        return Contract(id, reservaId, termos, dataGeracao)
    }

    fun all(): List<Contract> = transaction {
        Contracts.selectAll().map { it.toContract() }
    }
}