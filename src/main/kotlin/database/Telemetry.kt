package database

import org.joda.time.DateTime

// placeholder for a NoSQL telemetry store; for minimal scope we keep an in-memory list

data class TelemetryEvent(
    val vehicleId: Int,
    val timestamp: DateTime,
    val latitude: Double,
    val longitude: Double,
    val speedKmh: Double,
    val status: String
)

object TelemetryRepository {
    private val store = mutableListOf<TelemetryEvent>()

    fun add(event: TelemetryEvent): TelemetryEvent {
        store += event
        return event
    }

    fun allForVehicle(vehicleId: Int): List<TelemetryEvent> = store.filter { it.vehicleId == vehicleId }
}
