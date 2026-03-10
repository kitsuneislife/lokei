package database

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DatabaseTest {
    @BeforeTest
    fun setup() {
        DatabaseFactory.init()
    }

    @Test
    fun testInsertAndQuery() {
        val u1 = UserRepository.add("Alice")
        val u2 = UserRepository.add("Bob")

        val all = UserRepository.all().map { it.name }
        assertEquals(listOf("Alice", "Bob"), all)

        // add vehicle owned by u2
        val vehicle = VehicleRepository.add(
            proprietorId = u2.id,
            plate = "ABC1234",
            model = "Civic",
            year = 2020,
            photoUrls = null,
            dailyRate = 100.0,
            rentalPolicies = null,
            availabilityCalendar = null
        )
        assertEquals(u2.id, vehicle.proprietorId)

        // create reservation by u1 for the vehicle
        val now = org.joda.time.DateTime.now()
        val reservation = ReservationRepository.add(
            locatarioId = u1.id,
            veiculoId = vehicle.id,
            dataInicio = now,
            dataFim = now.plusDays(1),
            valorTotal = 100.0,
            status = "CONFIRMED"
        )
        assertEquals(u1.id, reservation.locatarioId)
        assertEquals(vehicle.id, reservation.veiculoId)

        // create payment & contract
        val payment = PaymentRepository.add(
            reservaId = reservation.id,
            valor = 100.0,
            metodo = "CARD",
            dataPagamento = now.plusHours(1),
            status = "PAID"
        )
        assertEquals(reservation.id, payment.reservaId)

        val contract = ContractRepository.add(
            reservaId = reservation.id,
            termos = "Terms text",
            dataGeracao = now.plusHours(1)
        )
        assertEquals(reservation.id, contract.reservaId)

        // add review by u1
        val review = ReviewRepository.add(
            reservaId = reservation.id,
            usuarioAvaliadorId = u1.id,
            pontuacao = 5,
            comentario = "Great!",
            data = now.plusDays(2)
        )
        assertEquals(u1.id, review.usuarioAvaliadorId)

        // telemetry store is an in-memory placeholder
        val ev = TelemetryRepository.add(
            TelemetryEvent(vehicleId = vehicle.id, timestamp = org.joda.time.DateTime.now(),
                latitude = -23.5, longitude = -46.6, speedKmh = 60.0, status = "OK")
        )
        assertEquals(vehicle.id, ev.vehicleId)
    }
}
