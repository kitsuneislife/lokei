package com.lukas.lokei.ui

data class OwnerProfile(
    val name: String,
    val rating: Double,
    val tripsCompleted: Int
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
            location = "Centro",
            dailyPrice = 249.0,
            rating = 4.8,
            seats = 5,
            transmission = "Automático",
            fuel = "Flex",
            photos = listOf("Foto frontal", "Foto lateral", "Interior"),
            owner = OwnerProfile("Marina Alves", 4.9, 63),
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
            location = "Zona Sul",
            dailyPrice = 159.0,
            rating = 4.5,
            seats = 5,
            transmission = "Manual",
            fuel = "Flex",
            photos = listOf("Foto frontal", "Painel", "Porta-malas"),
            owner = OwnerProfile("Roberto Lima", 4.7, 41),
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
            location = "Zona Norte",
            dailyPrice = 319.0,
            rating = 4.9,
            seats = 5,
            transmission = "Automático",
            fuel = "Híbrido",
            photos = listOf("Foto frontal", "Banco traseiro", "Multimídia"),
            owner = OwnerProfile("Paula Santos", 5.0, 88),
            reviews = listOf(
                VehicleReview("Ricardo", 5.0, "Experiência premium do início ao fim."),
                VehicleReview("Bianca", 4.8, "Muito silencioso e confortável.")
            ),
            terms = "Sem viagem interestadual sem aviso prévio; franquia de 350 km/dia."
        )
    )

    fun all(): List<ExploreVehicle> = vehicles

    fun byId(id: Int): ExploreVehicle? = vehicles.firstOrNull { it.id == id }
}
