package com.lukas.lokei.backend

data class SystemModuleStatus(
    val name: String,
    val description: String,
    val isImplemented: Boolean
)

object SystemArchitectureRepository {
    fun allModules(): List<SystemModuleStatus> = listOf(
        SystemModuleStatus(
            name = "API Gateway",
            description = "Roteia requisições do app para os módulos internos por rotas centralizadas.",
            isImplemented = true
        ),
        SystemModuleStatus(
            name = "Módulo de Autenticação",
            description = "Gerencia login, registro, sessão e perfis de acesso.",
            isImplemented = true
        ),
        SystemModuleStatus(
            name = "Módulo de Gestão de Veículos",
            description = "Centraliza cadastro, atualização e consulta de veículos.",
            isImplemented = true
        ),
        SystemModuleStatus(
            name = "Módulo de Busca e Reservas",
            description = "Aplica filtros, seleção de veículo e composição de reserva/checkout.",
            isImplemented = true
        ),
        SystemModuleStatus(
            name = "Módulo de Pagamentos",
            description = "Organiza cálculo de subtotal, taxas e método de pagamento escolhido.",
            isImplemented = true
        ),
        SystemModuleStatus(
            name = "Módulo de Monitoramento e Telemetria",
            description = "Consolida posição, velocidade e status de locação ativa.",
            isImplemented = true
        ),
        SystemModuleStatus(
            name = "Sistema de Inteligência Artificial (Algoritmo)",
            description = "Repositório de parâmetros para sugestão e precificação dinâmica.",
            isImplemented = true
        )
    )
}
