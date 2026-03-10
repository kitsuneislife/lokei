package database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object Vehicles : IntIdTable() {
    val proprietor = reference("proprietor_id", Users)
    val plate = varchar("plate", 20)
    val model = varchar("model", 100)
    val year = integer("year")
    val photoUrls = varchar("photo_urls", 1000).nullable()
    val dailyRate = decimal("daily_rate", 10, 2)
    val rentalPolicies = varchar("rental_policies", 1000).nullable()
    val availabilityCalendar = varchar("availability_calendar", 2000).nullable()
}

data class Vehicle(
    val id: Int,
    val proprietorId: Int,
    val plate: String,
    val model: String,
    val year: Int,
    val photoUrls: String?,
    val dailyRate: Double,
    val rentalPolicies: String?,
    val availabilityCalendar: String?
)

fun ResultRow.toVehicle() = Vehicle(
    id = this[Vehicles.id].value,
    proprietorId = this[Vehicles.proprietor].value,
    plate = this[Vehicles.plate],
    model = this[Vehicles.model],
    year = this[Vehicles.year],
    photoUrls = this[Vehicles.photoUrls],
    dailyRate = this[Vehicles.dailyRate].toDouble(),
    rentalPolicies = this[Vehicles.rentalPolicies],
    availabilityCalendar = this[Vehicles.availabilityCalendar]
)