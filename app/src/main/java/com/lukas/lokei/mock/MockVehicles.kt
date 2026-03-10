package com.lukas.lokei.mock

data class OwnerProfile(
    val name: String,
    val rating: Double,
    val tripsCompleted: Int,
    val username: String
)

data class VehicleReview(
    val author: String,
    val score: Double,
    val comment: String
)

data class ExploreVehicle(
    val id: Int,
    val title: String,
    val type: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val dailyPrice: Double,
    val rating: Double,
    val seats: Int,
    val transmission: String,
    val fuel: String,
    val photos: List<String>,
    val owner: OwnerProfile,
    val reviews: List<VehicleReview>,
    val terms: String
)

object ExploreVehicleRepository {
    private val vehicles = listOf(
        ExploreVehicle(
            id = 1,
            title = "Jeep Renegade 2022",
            type = "SUV",
            location = "Centro, Cascavel",
            latitude = -24.9555,
            longitude = -53.4552,
            dailyPrice = 249.0,
            rating = 4.8,
            seats = 5,
            transmission = "Automático",
            fuel = "Flex",
            photos = listOf("Foto frontal", "Foto lateral", "Interior"),
            owner = OwnerProfile("Proprietário Lokei", 4.9, 63, "proprietario"),
            reviews = listOf(
                VehicleReview("Carlos", 5.0, "Carro impecável e entrega rápida."),
                VehicleReview("Lia", 4.6, "Muito confortável para viagem.")
            ),
            terms = "Sem fumar, devolver com tanque no mesmo nível e limite de 300 km/dia."
        ),
        ExploreVehicle(
            id = 2,
            title = "Fiat Argo 2021",
            type = "Hatch",
            location = "Região do Lago, Cascavel",
            latitude = -24.9488,
            longitude = -53.4715,
            dailyPrice = 159.0,
            rating = 4.5,
            seats = 5,
            transmission = "Manual",
            fuel = "Flex",
            photos = listOf("Foto frontal", "Painel", "Porta-malas"),
            owner = OwnerProfile("Proprietário Lokei", 4.9, 63, "proprietario"),
            reviews = listOf(
                VehicleReview("Fernanda", 4.4, "Ótimo custo-benefício."),
                VehicleReview("João", 4.7, "Veículo econômico e limpo.")
            ),
            terms = "Permitido pet em caixa de transporte; franquia de 250 km/dia."
        ),
        ExploreVehicle(
            id = 3,
            title = "Toyota Corolla 2023",
            type = "Sedan",
            location = "Bairro Canadá, Cascavel",
            latitude = -24.9402,
            longitude = -53.4384,
            dailyPrice = 319.0,
            rating = 4.9,
            seats = 5,
            transmission = "Automático",
            fuel = "Híbrido",
            photos = listOf("Foto frontal", "Banco traseiro", "Multimídia"),
            owner = OwnerProfile("Proprietário Lokei", 4.9, 63, "proprietario"),
            reviews = listOf(
                VehicleReview("Ricardo", 5.0, "Experiência premium do início ao fim."),
                VehicleReview("Bianca", 4.8, "Muito silencioso e confortável.")
            ),
            terms = "Sem viagem interestadual sem aviso prévio; franquia de 350 km/dia."
        )
    )

    fun all(): List<ExploreVehicle> = vehicles

    fun byId(id: Int): ExploreVehicle? = vehicles.firstOrNull { it.id == id }

    fun forOwner(username: String): List<ExploreVehicle> = vehicles.filter { it.owner.username == username }
}
