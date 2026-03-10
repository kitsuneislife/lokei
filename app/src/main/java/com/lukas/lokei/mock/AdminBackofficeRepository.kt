package com.lukas.lokei.mock

import com.lukas.lokei.auth.AccountRepository
import kotlin.math.roundToInt

data class BackofficeKpi(
    val activeUsers: Int,
    val activeVehicles: Int,
    val activeReservations: Int,
    val grossVolume: Double,
    val platformCommission: Double,
    val dynamicPricingFactor: Double,
    val riskThreshold: Int
)

data class ModerationCase(
    val accountEmail: String,
    val name: String,
    val role: String,
    val isValidated: Boolean,
    val status: String
)

data class DisputeCase(
    val id: String,
    val reservationId: String,
    val claimant: String,
    val reason: String,
    val requestedAmount: Double,
    val status: String
)

object AdminBackofficeRepository {
    private var dynamicPricingFactor = 1.10
    private var riskThreshold = 70

    fun dashboardKpi(): BackofficeKpi {
        val accounts = AccountRepository.allAccounts()
        val vehicles = ExploreVehicleRepository.all()
        val reservations = maxOf(1, TelemetryRepository.activeSessionForUser("locatario")?.let { 1 } ?: 0)

        val grossVolume = vehicles.sumOf { it.dailyPrice } * reservations
        val platformCommission = grossVolume * 0.12

        return BackofficeKpi(
            activeUsers = accounts.size,
            activeVehicles = vehicles.size,
            activeReservations = reservations,
            grossVolume = grossVolume,
            platformCommission = platformCommission,
            dynamicPricingFactor = dynamicPricingFactor,
            riskThreshold = riskThreshold
        )
    }

    fun updatePricingFactor(value: Double) {
        dynamicPricingFactor = ((value * 100.0).roundToInt() / 100.0)
    }

    fun updateRiskThreshold(value: Int) {
        riskThreshold = value.coerceIn(0, 100)
    }

    fun moderationCases(): List<ModerationCase> =
        AccountRepository.allAccounts().map { account ->
            ModerationCase(
                accountEmail = account.email,
                name = account.name,
                role = account.role,
                isValidated = account.documentsValidated,
                status = if (account.documentsValidated) "Ativo" else "Pendente"
            )
        }

    fun disputeCases(): List<DisputeCase> {
        val vehicles = ExploreVehicleRepository.all()
        return vehicles.take(2).mapIndexed { index, vehicle ->
            DisputeCase(
                id = "DSP-${100 + index}",
                reservationId = "RSV-2026-00${index + 1}",
                claimant = if (index % 2 == 0) "locatario" else "proprietario",
                reason = if (index % 2 == 0) "Divergencia na devolucao" else "Solicitacao de cobertura por dano",
                requestedAmount = vehicle.dailyPrice * 1.5,
                status = "Em análise"
            )
        }
    }
}
