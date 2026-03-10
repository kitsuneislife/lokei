package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object VehicleRepository {
    fun add(
        proprietorId: Int,
        plate: String,
        model: String,
        year: Int,
        photoUrls: String?,
        dailyRate: Double,
        rentalPolicies: String?,
        availabilityCalendar: String?
    ): Vehicle {
        var id = 0
        transaction {
            id = Vehicles.insert {
                it[Vehicles.proprietor] = proprietorId
                it[Vehicles.plate] = plate
                it[Vehicles.model] = model
                it[Vehicles.year] = year
                it[Vehicles.photoUrls] = photoUrls
                it[Vehicles.dailyRate] = dailyRate.toBigDecimal()
                it[Vehicles.rentalPolicies] = rentalPolicies
                it[Vehicles.availabilityCalendar] = availabilityCalendar
            } get Vehicles.id
        }.value
        return Vehicle(id, proprietorId, plate, model, year, photoUrls, dailyRate, rentalPolicies, availabilityCalendar)
    }

    fun all(): List<Vehicle> = transaction {
        Vehicles.selectAll().map { it.toVehicle() }
    }
}