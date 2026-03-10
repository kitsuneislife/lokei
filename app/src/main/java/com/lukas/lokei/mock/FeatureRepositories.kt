package com.lukas.lokei.mock

import kotlin.math.roundToInt

data class ChatAttachment(
    val name: String,
    val type: String
)

data class ChatMessage(
    val id: String,
    val senderEmail: String,
    val text: String,
    val timestamp: String,
    val attachments: List<ChatAttachment> = emptyList()
)

data class ChatThread(
    val id: String,
    val title: String,
    val participantEmails: List<String>,
    val messages: List<ChatMessage>
)

object ChatRepository {
    private val threads = listOf(
        ChatThread(
            id = "thread-001",
            title = "Negociacao Jeep Renegade",
            participantEmails = listOf("proprietario", "locatario"),
            messages = listOf(
                ChatMessage("m1", "locatario", "Ola! O carro ainda esta disponivel para sexta?", "09:10"),
                ChatMessage("m2", "proprietario", "Esta sim. Posso entregar no Centro.", "09:14"),
                ChatMessage(
                    "m3",
                    "locatario",
                    "Perfeito, vou fechar a reserva.",
                    "09:18",
                    attachments = listOf(ChatAttachment("documento_locacao.pdf", "PDF"))
                )
            )
        )
    )

    fun threadsForUser(email: String): List<ChatThread> =
        threads.filter { email in it.participantEmails }
}

data class TelemetryPoint(
    val latitude: Double,
    val longitude: Double,
    val timestamp: String,
    val speedKmh: Int,
    val status: String
)

data class TelemetrySession(
    val reservationId: String,
    val vehicleId: Int,
    val vehicleTitle: String,
    val fuelPercent: Int,
    val alerts: List<String>,
    val points: List<TelemetryPoint>
)

object TelemetryRepository {
    private val sessions = listOf(
        TelemetrySession(
            reservationId = "RSV-2026-001",
            vehicleId = 1,
            vehicleTitle = ExploreVehicleRepository.byId(1)?.title ?: "Veiculo",
            fuelPercent = 72,
            alerts = listOf("Sem alertas criticos", "Velocidade dentro do limite"),
            points = listOf(
                TelemetryPoint(-24.9555, -53.4552, "10:05", 38, "Em movimento"),
                TelemetryPoint(-24.9521, -53.4492, "10:12", 42, "Em movimento"),
                TelemetryPoint(-24.9487, -53.4445, "10:19", 0, "Parado")
            )
        )
    )

    fun activeSessionForUser(email: String): TelemetrySession? {
        val ownsVehicle = sessions.any { session ->
            ExploreVehicleRepository.byId(session.vehicleId)?.owner?.username == email
        }
        return if (email == "locatario" || ownsVehicle) sessions.firstOrNull() else null
    }
}

data class MutualReviewDraft(
    val reservationId: String,
    val targetUser: String,
    val vehicleTitle: String
)

data class UserReviewHistory(
    val averageScore: Double,
    val received: List<VehicleReview>,
    val pendingMutual: List<MutualReviewDraft>
)

object ReviewHistoryRepository {
    fun historyForUser(email: String): UserReviewHistory {
        val ownedVehicles = ExploreVehicleRepository.all().filter { it.owner.username == email }
        val received = if (ownedVehicles.isNotEmpty()) {
            ownedVehicles.flatMap { it.reviews }
        } else {
            ExploreVehicleRepository.all().flatMap { it.reviews }
        }

        val average = if (received.isEmpty()) 0.0 else received.map { it.score }.average()

        val pending = listOf(
            MutualReviewDraft(
                reservationId = "RSV-2026-001",
                targetUser = if (email == "locatario") "Proprietario Lokei" else "Locatario Lokei",
                vehicleTitle = ExploreVehicleRepository.byId(1)?.title ?: "Veiculo"
            )
        )

        return UserReviewHistory(
            averageScore = ((average * 10.0).roundToInt() / 10.0),
            received = received,
            pendingMutual = pending
        )
    }
}

data class FaqItem(
    val question: String,
    val answer: String
)

data class SupportTicket(
    val id: String,
    val subject: String,
    val status: String
)

data class SupportCenterData(
    val faq: List<FaqItem>,
    val tickets: List<SupportTicket>
)

object SupportRepository {
    fun dataForUser(email: String): SupportCenterData {
        val faq = listOf(
            FaqItem("Como cancelar uma reserva?", "Acesse Reservas ativas, selecione a reserva e toque em Cancelar."),
            FaqItem("Como reportar avaria?", "Abra um chamado na central e anexe fotos/documentos no chat interno."),
            FaqItem("Quando recebo o estorno?", "Pagamentos via cartao podem levar ate 2 faturas conforme operadora.")
        )

        val tickets = listOf(
            SupportTicket("TCK-4101", "Duvida sobre caucao", "Em atendimento"),
            SupportTicket("TCK-4069", "Ajuste de horario de devolucao", "Resolvido")
        )

        return SupportCenterData(faq = faq, tickets = tickets)
    }

    fun nextTicketId(existingCount: Int): String = "TCK-${4100 + existingCount + 1}"
}

data class LoyaltyEntry(
    val description: String,
    val points: Int,
    val date: String
)

data class LoyaltyReward(
    val title: String,
    val cost: Int
)

data class LoyaltySummary(
    val balance: Int,
    val tier: String,
    val history: List<LoyaltyEntry>,
    val rewards: List<LoyaltyReward>
)

object LoyaltyRepository {
    fun summaryForUser(email: String): LoyaltySummary {
        val history = listOf(
            LoyaltyEntry("Reserva concluida", 120, "01/03/2026"),
            LoyaltyEntry("Indicacao aprovada", 80, "25/02/2026"),
            LoyaltyEntry("Uso de cupom", -40, "20/02/2026")
        )

        val rewards = listOf(
            LoyaltyReward("Cupom de R$ 30", 150),
            LoyaltyReward("Upgrade de categoria por 1 dia", 220),
            LoyaltyReward("Isencao de taxa de servico", 300)
        )

        return LoyaltySummary(
            balance = history.sumOf { it.points },
            tier = if (email == "proprietario") "Gold" else "Silver",
            history = history,
            rewards = rewards
        )
    }
}

data class ContractSummary(
    val id: String,
    val title: String,
    val period: String,
    val amount: String,
    val status: String
)

object ContractsRepository {
    fun listForUser(email: String, activeVehicleId: Int, rentalDays: Int): List<ContractSummary> {
        val activeVehicle = ExploreVehicleRepository.byId(activeVehicleId)
        val active = if (activeVehicle != null) {
            listOf(
                ContractSummary(
                    id = "LK-${activeVehicle.id}-ATIVO",
                    title = "${activeVehicle.title} - Contrato atual",
                    period = "$rentalDays dia(s)",
                    amount = "R$ ${"%.2f".format(activeVehicle.dailyPrice * rentalDays)}",
                    status = "Ativo"
                )
            )
        } else {
            emptyList()
        }

        val ownerVehicles = ExploreVehicleRepository.forOwner("proprietario")
        val history = ownerVehicles.take(2).mapIndexed { index, vehicle ->
            ContractSummary(
                id = "LK-HIST-${index + 1}",
                title = vehicle.title,
                period = "${index + 2} dia(s)",
                amount = "R$ ${"%.2f".format(vehicle.dailyPrice * (index + 2))}",
                status = "Finalizado"
            )
        }

        return active + history
    }
}
